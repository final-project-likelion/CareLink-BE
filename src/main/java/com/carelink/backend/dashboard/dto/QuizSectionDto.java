package com.carelink.backend.dashboard.dto;

import java.util.List;

public record QuizSectionDto(
        Integer todayScore,
        List<DailyQuizScoreDto> last7Days
) {}
