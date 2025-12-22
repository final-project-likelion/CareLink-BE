package com.carelink.backend.quiz.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class QuizResponseDto {

    private Long quizId;
    private String question;
    private List<String> options;
    private String formattedDate;   // "2025.12.22"
}
