package com.carelink.backend.userCondition.controller;

import com.carelink.backend.global.config.CustomUserDetails;
import com.carelink.backend.global.response.BaseResponse;
import com.carelink.backend.user.service.AuthService;
import com.carelink.backend.userCondition.dto.UserConditionRequestDto;
import com.carelink.backend.userCondition.dto.UserConditionResponseDto;
import com.carelink.backend.userCondition.service.UserConditionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/condition/today")
public class UserConditionController {

    private final UserConditionService userConditionService;
    private final AuthService authService;

    @GetMapping
    private ResponseEntity<BaseResponse<?>> getUserCondition(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        UserConditionResponseDto userCondition = userConditionService.getUserCondition(customUserDetails.getId());
        return ResponseEntity.ok()
                .body(BaseResponse.success("사용자의 오늘 컨디션 정보를 불러왔습니다.", userCondition));
    }

    @PostMapping
    private ResponseEntity<BaseResponse<?>> createUserCondition(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                @Valid @RequestBody UserConditionRequestDto userConditionRequestDto) {
        UserConditionResponseDto userCondition = userConditionService.createUserCondition(authService.getCurrentUser(customUserDetails.getId()), userConditionRequestDto);
        return ResponseEntity.ok()
                .body(BaseResponse.success("사용자의 오늘 컨디션 정보를 정상적으로 추가했습니다.", userCondition));
    }

    @PutMapping
    private ResponseEntity<BaseResponse<?>> updateUserCondition(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                @Valid @RequestBody UserConditionRequestDto userConditionRequestDto) {
        UserConditionResponseDto userCondition = userConditionService.updateUserCondition(customUserDetails.getId(), userConditionRequestDto);
        return ResponseEntity.ok()
                .body(BaseResponse.success("사용자의 오늘 컨디션 정보를 정상적으로 수정했습니다.", userCondition));
    }

}
