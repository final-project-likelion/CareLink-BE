package com.carelink.backend.quiz.service;

import com.carelink.backend.global.exception.BaseException;
import com.carelink.backend.global.exception.ErrorCode;
import com.carelink.backend.quiz.dto.*;
import com.carelink.backend.quiz.entity.*;
import com.carelink.backend.quiz.exception.QuizAlreadySolvedException;
import com.carelink.backend.quiz.repository.*;
import com.carelink.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizAttemptRepository quizAttemptRepository;

    public QuizResponseDto getTodayQuiz(User user) {

        Quiz quiz = quizRepository.findByDate(LocalDate.now())
                .orElseThrow(() -> new BaseException(ErrorCode.QUIZ_NOT_FOUND));

        boolean alreadySolved = quizAttemptRepository
                .findByUserIdAndQuiz_Id(user.getId(), quiz.getId())
                .isPresent();

        if (alreadySolved) {
            throw new QuizAlreadySolvedException();
        }

        return QuizResponseDto.builder()
                .quizId(quiz.getId())
                .date(quiz.getDate())
                .question(quiz.getQuestion())
                .options(List.of(
                        quiz.getOption1(),
                        quiz.getOption2(),
                        quiz.getOption3(),
                        quiz.getOption4()
                ))
                .build();
    }

    public QuizSubmitResponseDto submitQuiz(
            User user,
            Long quizId,
            QuizSubmitRequestDto request
    ) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow();

        quizAttemptRepository
                .findByUserIdAndQuiz_Id(user.getId(), quizId)
                .ifPresent(a -> {
                    throw new QuizAlreadySolvedException();
                });

        boolean isCorrect = quiz.getCorrectOption() == request.getSelectedOption();
        int score = isCorrect ? 100 : 0;

        QuizAttempt attempt = QuizAttempt.builder()
                .userId(user.getId())
                .quiz(quiz)
                .selectedOption(request.getSelectedOption())
                .isCorrect(isCorrect)
                .score(score)
                .solvedAt(LocalDateTime.now())
                .build();

        quizAttemptRepository.save(attempt);

        return QuizSubmitResponseDto.builder()
                .isCorrect(isCorrect)
                .correctOption(quiz.getCorrectOption())
                .score(score)
                .build();
    }
}
