package com.carelink.backend.quiz.repository;

import com.carelink.backend.quiz.entity.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    Optional<QuizAttempt> findByUserIdAndQuiz_Id(Long userId, Long quizId);
    boolean existsByUserIdAndSolvedDate(Long userId, LocalDate solvedDate);

}
