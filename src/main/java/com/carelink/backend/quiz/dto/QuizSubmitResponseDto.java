package com.carelink.backend.quiz.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizSubmitResponseDto {

    private boolean isCorrect;
    private int correctOption;
    private int score;
}
