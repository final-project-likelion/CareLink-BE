package com.carelink.backend.training.news.entity;

import com.carelink.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_news",
                        columnNames = {"user_id", "news_id"}
                ),
                @UniqueConstraint(
                        name = "uk_user_date",
                        columnNames = {"user_id", "completedDate"}
                )
        }
)
public class CognitiveTraining {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", nullable = false)
    private News news;

    @Column(nullable = false)
    private LocalDate completedDate;

    @Column(nullable = false)
    private Boolean completed;

    public CognitiveTraining(User user, News news, LocalDate completedDate) {
        this.user = user;
        this.news = news;
        this.completedDate = completedDate;
        this.completed = true;
    }
}
