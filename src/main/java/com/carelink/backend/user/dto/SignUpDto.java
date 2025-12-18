package com.carelink.backend.user.dto;

import com.carelink.backend.user.Category;
import com.carelink.backend.user.CognitiveState;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SignUpDto {

    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 4, message = "비밀번호는 4자 이상이어야 합니다.")
    private String password;

    @NotBlank
    @Pattern(
            regexp = "^010-?\\d{4}-?\\d{4}$",
            message = "휴대폰 번호 형식이 올바르지 않습니다."
    )
    private String phoneNum;

    @NotNull
    private LocalDate birthday;

    @NotNull
    private CognitiveState cognitiveState;

    @NotBlank
    private String caregiverName;

    @NotBlank
    @Pattern(
            regexp = "^010-?\\d{4}-?\\d{4}$",
            message = "휴대폰 번호 형식이 올바르지 않습니다."
    )
    private String caregiverPhoneNum;

    @NotBlank
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String caregiverEmail;

    @NotNull
    private List<Category> interestedCategory;

}
