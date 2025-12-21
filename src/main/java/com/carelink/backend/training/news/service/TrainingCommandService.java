package com.carelink.backend.training.news.service;

import com.carelink.backend.training.news.entity.*;
import com.carelink.backend.training.news.repository.CognitiveTrainingRepository;
import com.carelink.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class TrainingCommandService {

    private final CognitiveTrainingRepository trainingRepository;

    // 기사요약 2단계 완료 시 호출
    public void completeTraining(User user, News news) {

        if (trainingRepository.existsByUserIdAndNewsId(user.getId(), news.getId())) {
            throw new IllegalStateException("이미 훈련한 기사입니다.");
        }

        LocalDate today = LocalDate.now();
        if (trainingRepository.existsByUserIdAndCompletedDate(user.getId(), today)) {
            throw new IllegalStateException("하루에 하나의 훈련만 가능합니다.");
        }

        trainingRepository.save(
                new CognitiveTraining(user, news, today)
        );
    }
}
