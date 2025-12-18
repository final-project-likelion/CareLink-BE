package com.carelink.backend.chat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatRequestDto {

    @NotBlank
    private String question;

}
