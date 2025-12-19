package com.carelink.backend.training.news.service;

import com.carelink.backend.training.news.crawler.NaverNewsCrawler;
import com.carelink.backend.training.news.entity.News;
import com.carelink.backend.training.news.repository.NewsRepository;
import com.carelink.backend.training.news.ai.AiSummaryClient;
import com.carelink.backend.user.Category;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class DailyNewsCrawlingService {

    private final NaverNewsCrawler crawler;
    private final NewsRepository newsRepository;
    private final AiSummaryClient aiSummaryClient;

    public DailyNewsCrawlingService(
            NaverNewsCrawler crawler,
            NewsRepository newsRepository,
            AiSummaryClient aiSummaryClient
    ) {
        this.crawler = crawler;
        this.newsRepository = newsRepository;
        this.aiSummaryClient = aiSummaryClient;
    }

    @Transactional
    public void crawlDailyNews() {

        Set<String> usedUrls = new HashSet<>();

        for (Category category : Category.values()) {

            var crawled = crawler.crawlOneByCategory(
                    mapToNaverCode(category),
                    usedUrls
            );

            if (crawled == null) {
                continue;
            }

            News news = new News(
                    crawled.title(),
                    crawled.content(),
                    category
            );

            // AI 한줄요약 생성
            String previewSummary =
                    aiSummaryClient.generatePreviewSummary(news.getContent());

            // 엔티티에 요약 채우기
            news.updatePreview(previewSummary);

            // 기존 로직
            newsRepository.save(news);
        }
    }

    // 우리 카테고리와 네이버 뉴스의 카테고리를 임의로 매핑하기
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
