package com.carelink.backend.training.news.repository;

import com.carelink.backend.training.news.entity.CognitiveTraining;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    int countByUser_IdAndCompletedDateBetween(
            Long userId,
            LocalDate start,
            LocalDate end
    );

    // 오늘 기준으로 해당 유저가 완료한 뉴스 ID 목록
    @Query("""
        select ct.news.id
        from CognitiveTraining ct
        where ct.user.id = :userId
          and ct.completed = true
    """)
    List<Long> findCompletedNewsIds(Long userId);
}
