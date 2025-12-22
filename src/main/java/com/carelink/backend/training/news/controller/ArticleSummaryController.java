package com.carelink.backend.training.news.controller;

import com.carelink.backend.global.config.CustomUserDetails;
import com.carelink.backend.global.response.BaseResponse;
import com.carelink.backend.training.news.dto.*;
import com.carelink.backend.training.news.service.ArticleSummaryService;
import com.carelink.backend.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trainings/news")
public class ArticleSummaryController {

    private final ArticleSummaryService articleSummaryService;
    private final AuthService authService;

    @PostMapping("/{newsId}/summary")
    public BaseResponse<ArticleSummaryResultResponse> submitSummary(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long newsId,
            @RequestBody UserArticleSummarySubmitRequest request
    ) {
        return BaseResponse.success(
                articleSummaryService.submitSummary(authService.getCurrentUser(customUserDetails.getId()), newsId, request)
        );
    }
}
