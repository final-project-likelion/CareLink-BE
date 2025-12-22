package com.carelink.backend.user.controller;

import com.carelink.backend.global.response.BaseResponse;
import com.carelink.backend.user.dto.LoginDto;
import com.carelink.backend.user.dto.SignUpDto;
import com.carelink.backend.user.dto.TokenDto;
import com.carelink.backend.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    public ResponseEntity<BaseResponse<?>> signup(@Valid @RequestBody SignUpDto signUpDto) {
        Long id = authService.signUp(signUpDto);
        return ResponseEntity.ok()
                .body(BaseResponse.success("회원가입에 성공했습니다.", id));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ResponseEntity<BaseResponse<?>> login(@Valid @RequestBody LoginDto loginDto) {
        TokenDto tokens = authService.login(loginDto);
        return ResponseEntity.ok()
                .body(BaseResponse.success("로그인에 성공했습니다", tokens));
    }

    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발급")
    public ResponseEntity<BaseResponse<?>> reissue(@RequestBody String refreshToken) {
        TokenDto tokens = authService.reissue(refreshToken);
        return ResponseEntity.ok()
                .body(BaseResponse.success(tokens));
    }

    @PostMapping("/signup/duplicate-check")
    @Operation(summary = "아이디 중복체크")
    public ResponseEntity<BaseResponse<?>> isPhoneNumDuplicate(@RequestBody String phoneNum) {
        Boolean phoneNumDuplicate = authService.isPhoneNumDuplicate(phoneNum);
        return ResponseEntity.ok()
                .body(BaseResponse.success("아이디 중복 여부를 정상적으로 불러왔습니다.", phoneNumDuplicate));
    }

}
