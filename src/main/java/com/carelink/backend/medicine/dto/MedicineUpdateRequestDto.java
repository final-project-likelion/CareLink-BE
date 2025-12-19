package com.carelink.backend.medicine.dto;

import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class MedicineUpdateRequestDto {

    private String name;

    private List<IntakeTimeUpdateRequestDto> modifiedIntakeTimes;

    @Data
    public static class IntakeTimeUpdateRequestDto {
        private Long intakeTimeId;

        private LocalTime time;
    }

}
