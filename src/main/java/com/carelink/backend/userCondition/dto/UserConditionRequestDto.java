package com.carelink.backend.userCondition.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserConditionRequestDto {

    @NotNull
    @Min(value = 0, message = "점수는 최소 0점, 최대 2점이어야합니다.")
    @Max(value = 2, message = "점수는 최소 0점, 최대 2점이어야합니다.")
    private Integer moodScore;

    @NotNull
    @Min(value = 0, message = "점수는 최소 0점, 최대 2점이어야합니다.")
    @Max(value = 2, message = "점수는 최소 0점, 최대 2점이어야합니다.")
    private Integer sleepScore;

    @NotNull
    @Min(value = 0, message = "점수는 최소 0점, 최대 2점이어야합니다.")
    @Max(value = 2, message = "점수는 최소 0점, 최대 2점이어야합니다.")
    private Integer painScore;

}
