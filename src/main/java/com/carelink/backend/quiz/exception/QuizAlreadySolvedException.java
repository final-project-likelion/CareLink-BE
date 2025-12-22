package com.carelink.backend.quiz.exception;

import com.carelink.backend.global.exception.BaseException;
import com.carelink.backend.global.exception.ErrorCode;

public class QuizAlreadySolvedException extends BaseException {

    public QuizAlreadySolvedException() {
        super(ErrorCode.QUIZ_ALREADY_SOLVED);
    }
}
