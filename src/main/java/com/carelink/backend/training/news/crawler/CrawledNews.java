package com.carelink.backend.training.news.crawler;

public record CrawledNews(
        String title,
        String content,
        String thumbnailImageUrl // 네이버 원본 이미지 URL
) {
}
