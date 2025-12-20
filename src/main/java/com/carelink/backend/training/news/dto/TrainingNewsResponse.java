package com.carelink.backend.training.news.dto;

import com.carelink.backend.user.Category;
import lombok.Getter;

@Getter
public class TrainingNewsResponse {

    private Long newsId;
    private String title;
    private String content;
    private Category category;

    public TrainingNewsResponse(
            Long newsId,
            String title,
            String content,
            Category category
    ) {
        this.newsId = newsId;
        this.title = title;
        this.content = content;
        this.category = category;
    }
}
