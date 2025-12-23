package com.carelink.backend.dashboard.dto;

import java.util.List;

public record ConditionSectionDto(
        String status, // "좋음" | "보통" | "나쁨"
        List<Integer> moodScores
) {}
