package com.carelink.backend.quiz.controller;

import com.carelink.backend.global.config.CustomUserDetails;
import com.carelink.backend.global.response.BaseResponse;
import com.carelink.backend.quiz.dto.*;
import com.carelink.backend.quiz.service.QuizService;
import com.carelink.backend.user.entity.User;
import com.carelink.backend.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
public class QuizController {

    private final QuizService quizService;
    private final AuthService authService;

    @GetMapping("/")
    public BaseResponse<QuizResponseDto> getTodayQuiz(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return BaseResponse.success(
                quizService.getTodayQuiz(authService.getCurrentUser(customUserDetails.getId()))
        );
    }


    @PostMapping("/{quizId}/submit")
    public BaseResponse<QuizSubmitResponseDto> submitQuiz(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long quizId,
            @RequestBody QuizSubmitRequestDto request
    ) {
        return BaseResponse.success(
                quizService.submitQuiz(authService.getCurrentUser(customUserDetails.getId()), quizId, request)
        );
    }

}
