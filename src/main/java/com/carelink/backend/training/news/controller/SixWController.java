package com.carelink.backend.training.news.controller;

import com.carelink.backend.training.news.dto.*;
import com.carelink.backend.training.news.service.SixWService;
import com.carelink.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trainings/news")
public class SixWController {

    private final SixWService sixWService;

    @PostMapping("/{newsId}/sixw")
    public SixWResultResponse submitSixW(
            @PathVariable Long newsId,
            @RequestBody UserSixWSubmitRequest request,
            @AuthenticationPrincipal User user   // ⭐ 이게 핵심
    ) {
        return sixWService.submitSixWAnswer(user, newsId, request);
    }
}
