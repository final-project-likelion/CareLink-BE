package com.carelink.backend.training.news.repository;

import com.carelink.backend.training.news.entity.ArticleSummaryAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleSummaryAnswerRepository
        extends JpaRepository<ArticleSummaryAnswer, Long> {
}
