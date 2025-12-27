package com.carelink.backend.training.news.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class MonthlyTrainingSummary {

    private final int totalCount;
    private final Double averageScore;
    private final Integer maxScore;

    public MonthlyTrainingSummary(
            int totalCount,
            Double averageScore,
            Integer maxScore
    ) {
        this.totalCount = totalCount;
        this.averageScore = roundToTwoDecimalPlaces(averageScore);
        this.maxScore = maxScore;
    }

    private Double roundToTwoDecimalPlaces(Double value) {
        if (value == null) return null;
        return Math.round(value * 100) / 100.0;
    }
}

