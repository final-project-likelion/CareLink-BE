package com.carelink.backend.training.news.ai.dto;

public record SummaryScoreRequest(
        String article,
        String correct_summary,
        String user_summary
) {}
