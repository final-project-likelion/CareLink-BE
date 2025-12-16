package com.carelink.backend.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDto {

    @NotBlank
    private String phoneNum;

    @NotBlank
    private String password;

}
