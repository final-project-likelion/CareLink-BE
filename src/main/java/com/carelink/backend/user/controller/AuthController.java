package com.carelink.backend.user.controller;

import com.carelink.backend.global.response.BaseResponse;
import com.carelink.backend.user.dto.LoginDto;
import com.carelink.backend.user.dto.SignUpDto;
import com.carelink.backend.user.dto.TokenDto;
import com.carelink.backend.user.service.AuthService;
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
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<?>> signup(@Valid @RequestBody SignUpDto signUpDto) {
        Long id = authService.signUp(signUpDto);
        return ResponseEntity.ok()
                .body(BaseResponse.success("회원가입에 성공했습니다.", id));
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<?>> login(@Valid @RequestBody LoginDto loginDto) {
        TokenDto tokens = authService.login(loginDto);
        return ResponseEntity.ok()
                .body(BaseResponse.success("로그인에 성공했습니다", tokens));
    }

    @PostMapping("/reissue")
    public ResponseEntity<BaseResponse<?>> reissue(@RequestBody String refreshToken) {
        TokenDto tokens = authService.reissue(refreshToken);
        return ResponseEntity.ok()
                .body(BaseResponse.success(tokens));
    }

}
