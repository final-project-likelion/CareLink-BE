package com.carelink.backend.training.news.repository;

import com.carelink.backend.training.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
    // 최신 뉴스 8개 -> 꼭 8개 안 해도 되는 로직으로 바뀜
    List<News> findTop8ByOrderByCreatedDateDesc();
    boolean existsByTitle(String title);
    // 최근 N일 이내 뉴스 조회 (최신순)
    List<News> findByCreatedDateGreaterThanEqualOrderByCreatedDateDesc(
            LocalDate fromDate
    );
}
