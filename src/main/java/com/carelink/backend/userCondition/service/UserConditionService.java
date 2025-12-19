package com.carelink.backend.userCondition.service;

import com.carelink.backend.global.exception.BaseException;
import com.carelink.backend.global.exception.ErrorCode;
import com.carelink.backend.user.entity.User;
import com.carelink.backend.userCondition.dto.UserConditionRequestDto;
import com.carelink.backend.userCondition.dto.UserConditionResponseDto;
import com.carelink.backend.userCondition.entity.UserCondition;
import com.carelink.backend.userCondition.repository.UserConditionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserConditionService {

    private final UserConditionRepository userConditionRepository;

    public UserConditionResponseDto getUserCondition(Long userId) {
        UserCondition userCondition = userConditionRepository.findByUserIdAndDate(userId, LocalDate.now()).orElse(null);

        if (userCondition == null) return null;

        return UserConditionResponseDto.builder()
                .date(LocalDate.now())
                .moodScore(userCondition.getMoodScore())
                .sleepScore(userCondition.getSleepScore())
                .painScore(userCondition.getPainScore())
                .build();
    }

    @Transactional
    public UserConditionResponseDto createUserCondition(User user, UserConditionRequestDto userConditionRequestDto) {
         if (userConditionRepository.existsByUserIdAndDate(user.getId(), LocalDate.now()))
             throw new BaseException(ErrorCode.USER_CONDITION_ALREADY_EXISTS);

        UserCondition userCondition = UserCondition.builder()
                .user(user)
                .date(LocalDate.now())
                .moodScore(userConditionRequestDto.getMoodScore())
                .sleepScore(userConditionRequestDto.getSleepScore())
                .painScore(userConditionRequestDto.getPainScore()).build();

        userConditionRepository.save(userCondition);

        return UserConditionResponseDto.builder()
                .date(LocalDate.now())
                .moodScore(userCondition.getMoodScore())
                .sleepScore(userCondition.getSleepScore())
                .painScore(userCondition.getPainScore())
                .build();
    }

    @Transactional
    public UserConditionResponseDto updateUserCondition(Long userId, UserConditionRequestDto userConditionRequestDto) {
        UserCondition userCondition = userConditionRepository.findByUserIdAndDate(userId, LocalDate.now())
                .orElseThrow(() -> new BaseException(ErrorCode.USER_CONDITION_NOT_FOUND));

        userCondition.updateScores(userConditionRequestDto.getMoodScore(), userConditionRequestDto.getSleepScore(), userConditionRequestDto.getPainScore());

        return UserConditionResponseDto.builder()
                .date(LocalDate.now())
                .moodScore(userCondition.getMoodScore())
                .sleepScore(userCondition.getSleepScore())
                .painScore(userCondition.getPainScore())
                .build();
    }

}
