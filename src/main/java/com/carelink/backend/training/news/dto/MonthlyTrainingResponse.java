package com.carelink.backend.training.news.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class MonthlyTrainingResponse {
    private Long newsId;
    private String title;
    private LocalDate date;
    private Integer score;
}
