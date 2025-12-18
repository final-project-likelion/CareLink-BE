package com.carelink.backend.training.news.entity;

import com.carelink.backend.user.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Getter
@NoArgsConstructor
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private Category category;

    // 리스트용 한줄요약 (첫번째 페이지)
    @Column(length = 255)
    private String previewSummary;

    // 썸네일 이미지 URL
    private String thumbnailUrl;

    private LocalDate createdDate;

    public News(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.createdDate = LocalDate.now();
    }

    // 나중에 채우기용 setter
    public void updatePreview(String previewSummary, String thumbnailUrl) {
        this.previewSummary = previewSummary;
        this.thumbnailUrl = thumbnailUrl;
    }
}
