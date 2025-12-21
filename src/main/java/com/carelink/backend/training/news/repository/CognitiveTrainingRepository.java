package com.carelink.backend.training.news.repository;

import com.carelink.backend.training.news.entity.CognitiveTraining;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CognitiveTrainingRepository
        extends JpaRepository<CognitiveTraining, Long> {

    boolean existsByUserIdAndNewsId(Long userId, Long newsId);

    boolean existsByUserIdAndCompletedDate(Long userId, LocalDate date);

    List<CognitiveTraining> findByUserIdAndCompletedTrueAndCompletedDateBetween(
            Long userId,
            LocalDate start,
            LocalDate end
    );

    Optional<CognitiveTraining> findByUserIdAndNewsIdAndCompletedTrue(
            Long userId,
            Long newsId
    );
}
