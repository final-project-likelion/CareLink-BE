package com.carelink.backend.userDiary.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MonthlyDiaryDto {

    private Long id;

    private LocalDate date;

    private String title;

}
