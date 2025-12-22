package com.carelink.backend.training.news.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MonthlyTrainingSummary {

    private int totalCount;
    private Double averageScore;
    private Integer maxScore;
}
