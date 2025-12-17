package com.carelink.backend.medicine.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MedicineInfoDto {

    private Long id;

    private String name;

    private List<String> times;

}
