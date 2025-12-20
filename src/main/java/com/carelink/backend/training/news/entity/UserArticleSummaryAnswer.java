package com.carelink.backend.training.news.entity;

import com.carelink.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserArticleSummaryAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", nullable = false)
    private News news;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String summary;

    @Column(nullable = false)
    private Integer score;

    public UserArticleSummaryAnswer(
            User user,
            News news,
            String summary,
            Integer score
    ) {
        this.user = user;
        this.news = news;
        this.summary = summary;
        this.score = score;
    }
}
