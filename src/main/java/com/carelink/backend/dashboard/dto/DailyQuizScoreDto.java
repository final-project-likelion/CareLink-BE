package com.carelink.backend.dashboard.dto;

import java.time.LocalDate;

public record DailyQuizScoreDto(
        LocalDate date,
        Integer score
) {}
