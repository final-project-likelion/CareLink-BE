package com.carelink.backend.training.news.controller;

import com.carelink.backend.training.news.dto.*;
import com.carelink.backend.training.news.service.ArticleSummaryService;
import com.carelink.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trainings/news")
public class ArticleSummaryController {

    private final ArticleSummaryService articleSummaryService;

    @PostMapping("/{newsId}/summary")
    public ArticleSummaryResultResponse submitSummary(
            @PathVariable Long newsId,
            @RequestBody UserArticleSummarySubmitRequest request,
            @AuthenticationPrincipal User user
    ) {
        return articleSummaryService.submitSummary(user, newsId, request);
    }
}
