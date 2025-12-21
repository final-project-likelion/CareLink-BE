package com.carelink.backend.training.news.repository;

import com.carelink.backend.training.news.entity.UserArticleSummaryAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserArticleSummaryAnswerRepository
        extends JpaRepository<UserArticleSummaryAnswer, Long> {
    Optional<UserArticleSummaryAnswer> findByUserIdAndNewsId(Long userId, Long newsId);
}
