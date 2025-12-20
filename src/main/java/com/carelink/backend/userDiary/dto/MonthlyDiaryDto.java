package com.carelink.backend.userDiary.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class MonthlyDiaryDto {

    private String message;

    private List<DiaryDto> diaries;

    @Data
    @Builder
    public static class DiaryDto {
        private Long id;

        private LocalDate date;

        private String title;
    }

}
