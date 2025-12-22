package com.carelink.backend.training.news.controller;

import com.carelink.backend.global.config.CustomUserDetails;
import com.carelink.backend.global.response.BaseResponse;
import com.carelink.backend.training.news.dto.TrainingNewsResponse;
import com.carelink.backend.training.news.service.TrainingNewsService;
import com.carelink.backend.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trainings/news")
@RequiredArgsConstructor
public class TrainingNewsController {

    private final TrainingNewsService trainingNewsService;
    private final AuthService authService;

    // 인지훈련 진입용 뉴스 상세 조회
    @GetMapping("/{newsId}")
    public BaseResponse<TrainingNewsResponse> getTrainingNews(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long newsId
    ) {
        return BaseResponse.success(
                "인지훈련 진입이 가능합니다.",
                trainingNewsService.getTrainingNews(authService.getCurrentUser(customUserDetails.getId()), newsId)
        );
    }

}
