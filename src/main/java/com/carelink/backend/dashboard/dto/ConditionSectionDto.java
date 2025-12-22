package com.carelink.backend.dashboard.dto;

import java.util.List;

public record ConditionSectionDto(
        List<ConditionDto> last7Days
) {}
