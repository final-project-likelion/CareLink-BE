package com.carelink.backend.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginDto {

    @NotBlank
    @Pattern(
            regexp = "^010?\\d{4}?\\d{4}$",
            message = "휴대폰 번호 형식이 올바르지 않습니다."
    )
    private String phoneNum;

    @NotBlank
    private String password;

}
