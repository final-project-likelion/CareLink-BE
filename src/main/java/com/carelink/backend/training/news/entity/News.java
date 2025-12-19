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

    @Column(length = 255)
    private String previewSummary;

    private String thumbnailUrl;

    private LocalDate createdDate;

    public News(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.createdDate = LocalDate.now();
    }

    // 한줄요약만 채우는 메서드
    public void updatePreview(String previewSummary) {
        this.previewSummary = previewSummary;
    }

    // 기존 메서드
    public void updatePreview(String previewSummary, String thumbnailUrl) {
        this.previewSummary = previewSummary;
        this.thumbnailUrl = thumbnailUrl;
    }
}
