package com.carelink.backend.userCondition.controller;

import com.carelink.backend.global.response.BaseResponse;
import com.carelink.backend.user.entity.User;
import com.carelink.backend.userCondition.dto.UserConditionResponseDto;
import com.carelink.backend.userCondition.service.UserConditionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/condition/today")
public class UserConditionController {

    private final UserConditionService userConditionService;

    @GetMapping
    private ResponseEntity<BaseResponse<?>> getUserCondition(@AuthenticationPrincipal User user) {
        UserConditionResponseDto userCondition = userConditionService.getUserCondition(user.getId());
        return ResponseEntity.ok()
                .body(BaseResponse.success("사용자의 오늘 컨디션 정보를 불러왔습니다.", userCondition));
    }

}
