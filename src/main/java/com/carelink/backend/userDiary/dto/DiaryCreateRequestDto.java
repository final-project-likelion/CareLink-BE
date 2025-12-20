package com.carelink.backend.userDiary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DiaryCreateRequestDto {

    @NotBlank
    private String title;

    @NotNull
    private MultipartFile image;

}
