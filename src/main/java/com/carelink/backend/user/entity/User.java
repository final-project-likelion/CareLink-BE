package com.carelink.backend.user.entity;

import com.carelink.backend.user.CognitiveState;
import com.carelink.backend.userCondition.entity.UserCondition;
import com.carelink.backend.userInterest.entity.UserInterest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNum;

    @Column(nullable = false)
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CognitiveState cognitiveState;

    @OneToMany(mappedBy = "user")
    private List<UserInterest> userInterests = new ArrayList<>();

    @Column(nullable = false)
    private String caregiverName;

    @Column(nullable = false)
    private String caregiverPhoneNum;

    @Column(nullable = false)
    private String caregiverEmail;

    @OneToMany(mappedBy = "user")
    private List<UserCondition> userConditions = new ArrayList<>();

}