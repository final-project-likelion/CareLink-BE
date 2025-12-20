package com.carelink.backend.training.news.repository;

import com.carelink.backend.training.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
    // 최신 뉴스 8개
    List<News> findTop8ByOrderByCreatedDateDesc();
}
