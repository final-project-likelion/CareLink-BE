package com.carelink.backend.training.news.entity;

import com.carelink.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSixWAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", nullable = false)
    private News news;

    private String who;
    private String whenAt;
    private String whereAt;
    private String what;
    private String why;
    private String how;

    public UserSixWAnswer(
            User user,
            News news,
            String who,
            String whenAt,
            String whereAt,
            String what,
            String why,
            String how
    ) {
        this.user = user;
        this.news = news;
        this.who = who;
        this.whenAt = whenAt;
        this.whereAt = whereAt;
        this.what = what;
        this.why = why;
        this.how = how;
    }
}
