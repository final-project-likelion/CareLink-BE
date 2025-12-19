package com.carelink.backend.userCondition.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserConditionResponseDto {

    private int moodScore;

    private int sleepScore;

    private int painScore;

    private LocalDate date;

}
