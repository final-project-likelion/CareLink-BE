package com.carelink.backend.training.news.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralNewsDto {

    private Long newsId;
    private String title;
    private String thumbnailUrl;
    private int estimatedMinutes;
    private String previewSummary;
}
