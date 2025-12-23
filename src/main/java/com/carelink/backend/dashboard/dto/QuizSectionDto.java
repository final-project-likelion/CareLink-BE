package com.carelink.backend.dashboard.dto;

import java.util.List;

public record QuizSectionDto(
        String todayScore,   // "100" 또는 "0" 또는 "-" 반환
        List<Integer> scores
) {}

