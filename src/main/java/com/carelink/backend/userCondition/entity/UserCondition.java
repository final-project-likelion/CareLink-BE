package com.carelink.backend.userCondition.entity;

import com.carelink.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"date"})}
)
public class UserCondition {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer moodScore;

    @Column(nullable = false)
    private Integer sleepScore;

    @Column(nullable = false)
    private Integer painScore;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /** 점수 업데이트 */
    public void updateScores(Integer moodScore, Integer sleepScore, Integer painScore) {
        this.moodScore = moodScore;
        this.sleepScore = sleepScore;
        this.painScore = painScore;
    }

}
