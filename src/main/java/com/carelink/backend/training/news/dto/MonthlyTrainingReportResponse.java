package com.carelink.backend.training.news.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MonthlyTrainingReportResponse {

    private MonthlyTrainingSummary summary;
    private List<MonthlyTrainingResponse> trainings;
}
