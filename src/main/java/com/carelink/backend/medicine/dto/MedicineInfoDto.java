package com.carelink.backend.medicine.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MedicineInfoDto {

    private Long id;

    private String name;

    private List<MedicineIntakeTimeDto> times;

    @Data
    @Builder
    public static class MedicineIntakeTimeDto {
        private Long timeId;

        private String time;
    }

}
