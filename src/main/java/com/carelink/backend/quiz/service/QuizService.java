package com.carelink.backend.quiz.service;

import com.carelink.backend.quiz.dto.*;
import com.carelink.backend.quiz.entity.*;
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
                .orElseThrow(() -> new IllegalStateException("오늘의 퀴즈가 없습니다."));

        boolean alreadySolved =
                quizAttemptRepository.findByUserIdAndQuiz_Id(user.getId(), quiz.getId())
                        .isPresent();

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
                .alreadySolved(alreadySolved)
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
                    throw new IllegalStateException("이미 푼 퀴즈입니다.");
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
