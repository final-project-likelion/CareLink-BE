package com.carelink.backend.medicine.dto;

import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class MedicineUpdateRequestDto {

    private String name;

    private List<IntakeTimeDto> newIntakeTimes;

    @Data
    public static class IntakeTimeDto {
        private Long timeId;

        private LocalTime time;
    }

}
