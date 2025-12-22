package com.carelink.backend.quiz.repository;

import com.carelink.backend.quiz.entity.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    Optional<QuizAttempt> findByUserIdAndQuiz_Id(Long userId, Long quizId);
    boolean existsByUserIdAndSolvedDate(Long userId, LocalDate solvedDate);
    Integer findMaxScoreByUserIdAndSolvedDate(Long userId, LocalDate date);

    @Query("""
SELECT q.solvedDate, MAX(q.score)
FROM QuizAttempt q
WHERE q.userId = :userId
  AND q.solvedDate BETWEEN :start AND :end
GROUP BY q.solvedDate
""")
    List<Object[]> findDailyMaxScores(
            Long userId,
            LocalDate start,
            LocalDate end
    );

}
