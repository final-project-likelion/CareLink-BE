package com.carelink.backend.training.news.service;

import com.carelink.backend.training.news.ai.AiSixWClient;
import com.carelink.backend.training.news.ai.AiSummaryClient;
import com.carelink.backend.training.news.ai.dto.SixWResponseDto;
import com.carelink.backend.training.news.crawler.NaverNewsCrawler;
import com.carelink.backend.training.news.entity.News;
import com.carelink.backend.training.news.entity.SixWAnswer;
import com.carelink.backend.training.news.repository.NewsRepository;
import com.carelink.backend.training.news.repository.SixWAnswerRepository;
import com.carelink.backend.user.Category;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.carelink.backend.training.news.ai.AiArticleSummaryClient;
import com.carelink.backend.training.news.ai.dto.ArticleSummaryResponseDto;
import com.carelink.backend.training.news.entity.ArticleSummaryAnswer;
import com.carelink.backend.training.news.repository.ArticleSummaryAnswerRepository;


import java.util.HashSet;
import java.util.Set;

@Service
public class DailyNewsCrawlingService {

    private final NaverNewsCrawler crawler;
    private final NewsRepository newsRepository;
    private final SixWAnswerRepository sixWAnswerRepository;
    private final AiSummaryClient aiSummaryClient;
    private final AiSixWClient aiSixWClient;
    private final ArticleSummaryAnswerRepository articleSummaryAnswerRepository;
    private final AiArticleSummaryClient aiArticleSummaryClient;


    public DailyNewsCrawlingService(
            NaverNewsCrawler crawler,
            NewsRepository newsRepository,
            SixWAnswerRepository sixWAnswerRepository,
            ArticleSummaryAnswerRepository articleSummaryAnswerRepository,
            AiSummaryClient aiSummaryClient,
            AiSixWClient aiSixWClient,
            AiArticleSummaryClient aiArticleSummaryClient
    ) {
        this.crawler = crawler;
        this.newsRepository = newsRepository;
        this.sixWAnswerRepository = sixWAnswerRepository;
        this.articleSummaryAnswerRepository = articleSummaryAnswerRepository;
        this.aiSummaryClient = aiSummaryClient;
        this.aiSixWClient = aiSixWClient;
        this.aiArticleSummaryClient = aiArticleSummaryClient;
    }


    @Transactional
    public void crawlDailyNews() {

        Set<String> usedUrls = new HashSet<>();

        for (Category category : Category.values()) {

            var crawled = crawler.crawlOneByCategory(
                    mapToNaverCode(category),
                    usedUrls
            );

            if (crawled == null) continue;

            News news = new News(
                    crawled.title(),
                    crawled.content(),
                    category
            );

            // 1. AI 한줄 요약
            String previewSummary =
                    aiSummaryClient.generatePreviewSummary(news.getContent());
            news.updatePreview(previewSummary);

            newsRepository.save(news);

            // 2. 육하원칙 정답 생성
            SixWResponseDto sixw =
                    aiSixWClient.generateSixW(news.getTitle(), news.getContent());

            SixWAnswer answer = new SixWAnswer(
                    news,
                    sixw.getWho(),
                    sixw.getWhen(),
                    sixw.getWhere(),
                    sixw.getWhat(),
                    sixw.getWhy(),
                    sixw.getHow()
            );

            sixWAnswerRepository.save(answer);

            // 3. 기사요약 정답 생성
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
