package com.carelink.backend.userDiary.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserDiaryDto {

    private Long id;

    private String title;

    private LocalDate localDate;

    private String imageUrl;

}
