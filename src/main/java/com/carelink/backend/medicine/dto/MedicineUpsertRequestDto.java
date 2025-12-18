package com.carelink.backend.medicine.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class MedicineUpsertRequestDto {

    @NotBlank
    private String name;

    @NotNull
    @NotEmpty
    private List<LocalTime> times;

}
