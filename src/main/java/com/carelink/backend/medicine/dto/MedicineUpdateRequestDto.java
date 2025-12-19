package com.carelink.backend.medicine.dto;

import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class MedicineUpdateRequestDto {

    private String name;

    private List<IntakeTimeDto> modifiedIntakeTimes;

    @Data
    public static class IntakeTimeDto {
        private Long intakeTimeId;

        private LocalTime time;
    }

}
