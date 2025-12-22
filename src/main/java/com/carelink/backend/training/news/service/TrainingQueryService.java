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


    // 월별 인지훈련 목록 조회
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

    // 월별 인지훈련 리포트 조회
    public MonthlyTrainingReportResponse getMonthlyTrainingReport(
            Long userId,
            YearMonth month
    ) {
        List<MonthlyTrainingResponse> trainings =
                getMonthlyTrainings(userId, month);

        int totalCount = trainings.size();

        // 점수 null 제외
        List<Integer> scores = trainings.stream()
                .map(MonthlyTrainingResponse::getScore)
                .filter(score -> score != null)
                .toList();

        Double averageScore =
                scores.isEmpty()
                        ? null
                        : scores.stream()
                        .mapToInt(Integer::intValue)
                        .average()
                        .orElse(0.0);

        Integer maxScore =
                scores.isEmpty()
                        ? null
                        : scores.stream().max(Integer::compareTo).orElse(null);

        MonthlyTrainingSummary summary =
                new MonthlyTrainingSummary(
                        totalCount,
                        averageScore,
                        maxScore
                );

        return new MonthlyTrainingReportResponse(
                summary,
                trainings
        );
    }


    // 인지훈련 개별조회
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
