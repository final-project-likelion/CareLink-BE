package com.carelink.backend.training.news.dto;

public record ArticleSummaryResultResponse(
        ArticleSummaryContent correctAnswer,
        Integer score
) {}
