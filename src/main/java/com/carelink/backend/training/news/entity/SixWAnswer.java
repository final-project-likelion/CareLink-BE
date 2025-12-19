package com.carelink.backend.training.news.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SixWAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", nullable = false)
    private News news;

    @Column(nullable = false, length = 255)
    private String who;

    @Column(nullable = false, length = 255)
    private String whenAt;

    @Column(nullable = false, length = 255)
    private String whereAt;

    @Column(nullable = false, length = 500)
    private String what;

    @Column(nullable = false, length = 500)
    private String why;

    @Column(nullable = false, length = 255)
    private String how;

    public SixWAnswer(
            News news,
            String who,
            String whenAt,
            String whereAt,
            String what,
            String why,
            String how
    ) {
        this.news = news;
        this.who = who;
        this.whenAt = whenAt;
        this.whereAt = whereAt;
        this.what = what;
        this.why = why;
        this.how = how;
    }
}
