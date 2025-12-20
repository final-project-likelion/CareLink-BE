package com.carelink.backend.training.news.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SixWResultResponse {

    private Long newsId;

    private CorrectSixWAnswer correctAnswer;

    private String message;

}
