package com.carelink.backend.userCondition.service;

import com.carelink.backend.userCondition.dto.UserConditionResponseDto;
import com.carelink.backend.userCondition.entity.UserCondition;
import com.carelink.backend.userCondition.repository.UserConditionRepository;
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

}
