package com.carelink.backend.training.news.repository;

import com.carelink.backend.training.news.entity.ArticleSummaryAnswer;
import com.carelink.backend.training.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleSummaryAnswerRepository
        extends JpaRepository<ArticleSummaryAnswer, Long> {
    Optional<ArticleSummaryAnswer> findByNews(News news);
    Optional<ArticleSummaryAnswer> findByNewsId(Long newsId);
}
