package com.carelink.backend.training.news.service;

import com.carelink.backend.training.news.ai.AiArticleSummaryClient;
import com.carelink.backend.training.news.ai.AiNewsFilterClient;
import com.carelink.backend.training.news.ai.AiSixWClient;
import com.carelink.backend.training.news.ai.AiSummaryClient;
import com.carelink.backend.training.news.ai.dto.ArticleSummaryResponseDto;
import com.carelink.backend.training.news.ai.dto.SixWResponseDto;
import com.carelink.backend.training.news.crawler.CrawledNews;
import com.carelink.backend.training.news.crawler.NaverNewsCrawler;
import com.carelink.backend.training.news.entity.ArticleSummaryAnswer;
import com.carelink.backend.training.news.entity.News;
import com.carelink.backend.training.news.entity.SixWAnswer;
import com.carelink.backend.training.news.filter.NewsHardFilter;
import com.carelink.backend.training.news.repository.ArticleSummaryAnswerRepository;
import com.carelink.backend.training.news.repository.NewsRepository;
import com.carelink.backend.training.news.repository.SixWAnswerRepository;
import com.carelink.backend.user.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DailyNewsCrawlingService {

    private final NaverNewsCrawler crawler;
    private final NewsRepository newsRepository;
    private final SixWAnswerRepository sixWAnswerRepository;
    private final ArticleSummaryAnswerRepository articleSummaryAnswerRepository;
    private final AiSummaryClient aiSummaryClient;
    private final AiSixWClient aiSixWClient;
    private final AiArticleSummaryClient aiArticleSummaryClient;
    private final ThumbnailUploader thumbnailUploader;
    private final AiNewsFilterClient aiNewsFilterClient;


    @Transactional
    public void crawlDailyNews() {

        Set<String> usedUrls = new HashSet<>();

        for (Category category : Category.values()) {


            boolean saved = false;
            int tryCount = 0;

            while (!saved && tryCount < 15) {
                tryCount++;

                CrawledNews crawled =
                        crawler.crawlOneByCategory(
                                mapToNaverCode(category),
                                usedUrls
                        );

                if (crawled == null) break;

                String title = crawled.title();
                String content = crawled.content();

                /* ~~ !! 필터링 !! ~~ */
                // 1차: 제목 하드 필터 (정말 안 되는 키워드 직접 거르기)
                if (NewsHardFilter.isBlocked(title)) {
                    continue;
                }

                // 2차: AI usable 필터 (fast api 필터링 응답결과)
                if (!aiNewsFilterClient.isUsable(title, content)) {
                    continue;
                }

                // 3차: 이미 저장된 기사면 스킵 (중복 생성 방지)
                if (newsRepository.existsByTitle(title)) continue;


                // 위의 필터를 모두 통과해야 뉴스 저장 및 정답생성 파이프라인으로 넘어감
                News news = new News(title, content, category);

                // 0. 썸네일 업로드 (네이버 원본 → S3)
                if (crawled.thumbnailImageUrl() != null) {
                    String s3ThumbnailUrl =
                            thumbnailUploader.uploadFromUrl(
                                    crawled.thumbnailImageUrl()
                            );

                    if (s3ThumbnailUrl != null) {
                        news.updatePreview(null, s3ThumbnailUrl);
                    }
                }

                // 1. AI 한줄 요약
                String previewSummary =
                        aiSummaryClient.generatePreviewSummary(news.getContent());
                news.updatePreview(previewSummary);

                // 2. 뉴스 저장
                newsRepository.save(news);

                // 3. 육하원칙 정답 생성
                SixWResponseDto sixw =
                        aiSixWClient.generateSixW(news.getTitle(), news.getContent());

                SixWAnswer sixWAnswer = new SixWAnswer(
                        news,
                        sixw.getWho(),
                        sixw.getWhen(),
                        sixw.getWhere(),
                        sixw.getWhat(),
                        sixw.getWhy(),
                        sixw.getHow()
                );
                sixWAnswerRepository.save(sixWAnswer);

                // 4. 기사 요약 정답 생성
                ArticleSummaryResponseDto articleSummary =
                        aiArticleSummaryClient.generateArticleSummary(
                                news.getTitle(),
                                news.getContent()
                        );

                ArticleSummaryAnswer summaryAnswer =
                        new ArticleSummaryAnswer(
                                news,
                                articleSummary.getSummary()
                        );
                articleSummaryAnswerRepository.save(summaryAnswer);
                // 기사를 저장하면 saved 값을 true로 바꿔서 중복 저장 방지
                saved = true;
            }
        }
    }

    private String mapToNaverCode(Category category) {
        return switch (category) {
            case HEALTH -> "103";
            case WELFARE_POLICY -> "100";
            case PETS -> "103";
            case SOCIETY -> "102";
            case PLANTS -> "103";
            case FOOD -> "103";
            case TRAVEL -> "103";
            case HOBBY_CULTURE -> "103";
        };
    }
}
