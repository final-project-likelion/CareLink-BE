package com.carelink.backend.dashboard.dto;

public record DashboardResponseDto(
        ConditionSectionDto condition,
        WeeklyStatusDto medicine,
        WeeklyStatusDto diary,
        WeeklyStatusDto cognitiveTraining,
        QuizSectionDto quiz
) {}
