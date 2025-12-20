package com.carelink.backend.training.news.service;

import com.carelink.backend.training.news.ai.AiSummaryScoreClient;
import com.carelink.backend.training.news.dto.*;
import com.carelink.backend.training.news.entity.*;
import com.carelink.backend.training.news.repository.*;
import com.carelink.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleSummaryService {

    private final NewsRepository newsRepository;
    private final ArticleSummaryAnswerRepository articleSummaryAnswerRepository;
    private final UserArticleSummaryAnswerRepository userArticleSummaryAnswerRepository;
    private final AiSummaryScoreClient aiSummaryScoreClient;

    public ArticleSummaryResultResponse submitSummary(
            User user,
            Long newsId,
            UserArticleSummarySubmitRequest request
    ) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new IllegalArgumentException("뉴스 없음"));

        ArticleSummaryAnswer correct =
                articleSummaryAnswerRepository.findByNews(news)
                        .orElseThrow(() -> new IllegalStateException("모범 요약 없음"));

        Integer score = aiSummaryScoreClient.scoreSummary(
                news.getContent(),
                correct.getSummary(),
                request.summary()
        );

        UserArticleSummaryAnswer userAnswer =
                new UserArticleSummaryAnswer(
                        user,
                        news,
                        request.summary(),
                        score
                );
        userArticleSummaryAnswerRepository.save(userAnswer);

        return new ArticleSummaryResultResponse(
                new ArticleSummaryContent(
                        news.getId(),
                        correct.getSummary()
                ),
                score
        );
    }
}
