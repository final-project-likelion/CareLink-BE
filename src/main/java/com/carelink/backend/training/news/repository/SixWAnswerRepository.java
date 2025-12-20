package com.carelink.backend.training.news.repository;

import com.carelink.backend.training.news.entity.News;
import com.carelink.backend.training.news.entity.SixWAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SixWAnswerRepository extends JpaRepository<SixWAnswer, Long> {
    Optional<SixWAnswer> findByNews(News news);
}
