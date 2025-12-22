package com.carelink.backend.quiz.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class QuizResponseDto {

    private Long quizId;
    private LocalDate date;
    private String question;
    private List<String> options;
}
