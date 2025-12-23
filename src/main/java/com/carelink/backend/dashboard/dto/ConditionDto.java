package com.carelink.backend.dashboard.dto;

import java.time.LocalDate;

public record ConditionDto(
        LocalDate date,
        Integer moodScore
) {}
