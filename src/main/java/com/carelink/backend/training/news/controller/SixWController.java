package com.carelink.backend.training.news.controller;

import com.carelink.backend.global.config.CustomUserDetails;
import com.carelink.backend.global.response.BaseResponse;
import com.carelink.backend.training.news.dto.*;
import com.carelink.backend.training.news.service.SixWService;
import com.carelink.backend.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trainings/news")
public class SixWController {

    private final SixWService sixWService;
    private final AuthService authService;

    @PostMapping("/{newsId}/sixw")
    public BaseResponse<SixWResultResponse> submitSixW(
            @PathVariable Long newsId,
            @RequestBody UserSixWSubmitRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return BaseResponse.success(
                sixWService.submitSixWAnswer(authService.getCurrentUser(customUserDetails.getId()), newsId, request)
        );
    }
}
