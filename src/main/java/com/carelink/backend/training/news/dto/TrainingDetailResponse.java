package com.carelink.backend.training.news.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TrainingDetailResponse {

    private String newsTitle;
    private String newsContent;

    private SixWDto systemSixW;
    private SixWDto userSixW;

    private String systemSummary;
    private String userSummary;
}
