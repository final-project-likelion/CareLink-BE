package com.carelink.backend.training.news.repository;

import com.carelink.backend.training.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {
}
