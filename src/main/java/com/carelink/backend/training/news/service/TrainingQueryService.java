package com.carelink.backend.training.news.service;

import com.carelink.backend.training.news.dto.*;
import com.carelink.backend.training.news.entity.*;
import com.carelink.backend.training.news.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrainingQueryService {

    private final CognitiveTrainingRepository trainingRepository;
    private final UserArticleSummaryAnswerRepository userSummaryRepository;
    private final SixWAnswerRepository sixWAnswerRepository;
    private final UserSixWAnswerRepository userSixWAnswerRepository;
    private final ArticleSummaryAnswerRepository articleSummaryAnswerRepository;
    private final NewsRepository newsRepository;

    public List<MonthlyTrainingResponse> getMonthlyTrainings(
            Long userId,
            YearMonth month
    ) {
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();

        return trainingRepository
                .findByUserIdAndCompletedTrueAndCompletedDateBetween(userId, start, end)
                .stream()
                .map(t -> new MonthlyTrainingResponse(
                        t.getNews().getId(),
                        t.getNews().getTitle(),
                        t.getCompletedDate(),
                        userSummaryRepository
                                .findByUserIdAndNewsId(userId, t.getNews().getId())
                                .map(UserArticleSummaryAnswer::getScore)
                                .orElse(null)
                ))
                .toList();
    }
    public TrainingDetailResponse getTrainingDetail(Long userId, Long newsId) {

        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new IllegalArgumentException("뉴스 없음"));

        SixWAnswer systemSixW =
                sixWAnswerRepository.findByNewsId(newsId).orElseThrow();

        UserSixWAnswer userSixW =
                userSixWAnswerRepository
                        .findByUserIdAndNewsId(userId, newsId)
                        .orElseThrow();

        ArticleSummaryAnswer systemSummary =
                articleSummaryAnswerRepository.findByNewsId(newsId).orElseThrow();

        UserArticleSummaryAnswer userSummary =
                userSummaryRepository
                        .findByUserIdAndNewsId(userId, newsId)
                        .orElseThrow();

        return new TrainingDetailResponse(
                news.getTitle(),
                news.getContent(),
                new SixWDto(
                        systemSixW.getWho(),
                        systemSixW.getWhenAt(),
                        systemSixW.getWhereAt(),
                        systemSixW.getWhat(),
                        systemSixW.getWhy(),
                        systemSixW.getHow()
                ),
                new SixWDto(
                        userSixW.getWho(),
                        userSixW.getWhenAt(),
                        userSixW.getWhereAt(),
                        userSixW.getWhat(),
                        userSixW.getWhy(),
                        userSixW.getHow()
                ),
                systemSummary.getSummary(),
                userSummary.getSummary()
        );
    }

}
