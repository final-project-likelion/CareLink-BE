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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizAttemptRepository quizAttemptRepository;

    public QuizResponseDto getTodayQuiz(User user) {

        // 1. 오늘 이미 퀴즈를 풀었으면 차단
        boolean alreadySolvedToday =
                quizAttemptRepository.existsByUserIdAndSolvedDate(
                        user.getId(),
                        LocalDate.now()
                );

        if (alreadySolvedToday) {
            throw new QuizAlreadySolvedException();
        }

        // 2. 퀴즈 풀(pool) 전체 조회
        List<Quiz> quizzes = quizRepository.findAll();

        if (quizzes.isEmpty()) {
            throw new BaseException(ErrorCode.QUIZ_NOT_FOUND);
        }

        // 3. 랜덤 1개 선택
        Quiz randomQuiz = quizzes.get(
                ThreadLocalRandom.current().nextInt(quizzes.size())
        );

        // 4. 응답
        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));

        return QuizResponseDto.builder()
                .quizId(randomQuiz.getId())
                .question(randomQuiz.getQuestion())
                .options(List.of(
                        randomQuiz.getOption1(),
                        randomQuiz.getOption2(),
                        randomQuiz.getOption3(),
                        randomQuiz.getOption4()
                ))
                .formattedDate(formattedDate)
                .build();
    }

    public QuizSubmitResponseDto submitQuiz(
            User user,
            Long quizId,
            QuizSubmitRequestDto request
    ) {

        // 1. 오늘 이미 풀었으면 차단 (조회 우회 방지)
        if (quizAttemptRepository.existsByUserIdAndSolvedDate(
                user.getId(),
                LocalDate.now()
        )) {
            throw new QuizAlreadySolvedException();
        }

        // 2. 퀴즈 조회
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new BaseException(ErrorCode.QUIZ_NOT_FOUND));

        // 3. 정답 판별
        boolean isCorrect = quiz.getCorrectOption() == request.getSelectedOption();
        int score = isCorrect ? 100 : 0;

        // 4. 풀이 기록 저장
        QuizAttempt attempt = QuizAttempt.builder()
                .userId(user.getId())
                .quiz(quiz)
                .selectedOption(request.getSelectedOption())
                .isCorrect(isCorrect)
                .score(score)
                .solvedDate(LocalDate.now())
                .build();

        quizAttemptRepository.save(attempt);

        // 5. 응답
        return QuizSubmitResponseDto.builder()
                .isCorrect(isCorrect)
                .correctOption(quiz.getCorrectOption())
                .score(score)
                .build();
    }
}
