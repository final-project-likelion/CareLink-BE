package com.carelink.backend.training.news.repository;

import com.carelink.backend.training.news.entity.UserArticleSummaryAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserArticleSummaryAnswerRepository
        extends JpaRepository<UserArticleSummaryAnswer, Long> {
}
