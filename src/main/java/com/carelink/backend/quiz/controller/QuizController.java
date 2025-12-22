package com.carelink.backend.quiz.controller;

import com.carelink.backend.global.response.BaseResponse;
import com.carelink.backend.quiz.dto.*;
import com.carelink.backend.quiz.service.QuizService;
import com.carelink.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
public class QuizController {

    private final QuizService quizService;

    @GetMapping("/")
    public BaseResponse<QuizResponseDto> getTodayQuiz(
            @AuthenticationPrincipal User user
    ) {
        return BaseResponse.success(
                quizService.getTodayQuiz(user)
        );
    }


    @PostMapping("/{quizId}/submit")
    public BaseResponse<QuizSubmitResponseDto> submitQuiz(
            @AuthenticationPrincipal User user,
            @PathVariable Long quizId,
            @RequestBody QuizSubmitRequestDto request
    ) {
        return BaseResponse.success(
                quizService.submitQuiz(user, quizId, request)
        );
    }

}
