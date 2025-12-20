package com.carelink.backend.training.news.service;

import com.carelink.backend.training.news.ai.AiArticleSummaryClient;
import com.carelink.backend.training.news.ai.AiSixWClient;
import com.carelink.backend.training.news.ai.AiSummaryClient;
import com.carelink.backend.training.news.ai.dto.ArticleSummaryResponseDto;
import com.carelink.backend.training.news.ai.dto.SixWResponseDto;
import com.carelink.backend.training.news.crawler.CrawledNews;
import com.carelink.backend.training.news.crawler.NaverNewsCrawler;
import com.carelink.backend.training.news.entity.ArticleSummaryAnswer;
import com.carelink.backend.training.news.entity.News;
import com.carelink.backend.training.news.entity.SixWAnswer;
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

    @Transactional
    public void crawlDailyNews() {

        Set<String> usedUrls = new HashSet<>();

        for (Category category : Category.values()) {

            CrawledNews crawled =
                    crawler.crawlOneByCategory(
                            mapToNaverCode(category),
                            usedUrls
                    );

            if (crawled == null) continue;

            News news = new News(
                    crawled.title(),
                    crawled.content(),
                    category
            );

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
