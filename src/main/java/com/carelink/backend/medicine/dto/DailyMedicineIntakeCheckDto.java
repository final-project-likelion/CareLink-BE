package com.carelink.backend.medicine.dto;

import lombok.Data;

import java.util.List;

@Data
public class DailyMedicineIntakeCheckDto {

    List<Long> medicineIntakeTimeIds;

}
