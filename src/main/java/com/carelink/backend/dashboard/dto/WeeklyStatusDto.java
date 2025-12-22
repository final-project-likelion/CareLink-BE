package com.carelink.backend.dashboard.dto;

public record WeeklyStatusDto(
        String todayStatus,   // "완료" | "작성 전"
        int completedDays,
        int percentage
) {}
