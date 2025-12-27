package com.carelink.backend.training.news.service;

import com.carelink.backend.training.news.dto.TrainingNewsResponse;
import com.carelink.backend.training.news.entity.News;
import com.carelink.backend.training.news.repository.CognitiveTrainingRepository;
import com.carelink.backend.training.news.repository.NewsRepository;
import com.carelink.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
@Service
@RequiredArgsConstructor
public class TrainingNewsService {

    private final NewsRepository newsRepository;
    private final CognitiveTrainingRepository trainingRepository;

    @Transactional(readOnly = true)
    public TrainingNewsResponse getTrainingNews(User user, Long newsId) {

        // 1. 같은 뉴스 재훈련 방지
        if (trainingRepository.existsByUserIdAndNewsId(user.getId(), newsId)) {
            throw new IllegalStateException("이미 훈련을 완료한 기사입니다.");
        }

        // 2. 하루 1회 제한
        if (trainingRepository.existsByUserIdAndCompletedTrueAndCompletedDate(
                user.getId(), LocalDate.now())) {
            throw new IllegalStateException("오늘은 이미 인지훈련을 완료했습니다.");
        }

        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new IllegalArgumentException("뉴스를 찾을 수 없습니다. id=" + newsId));

        return new TrainingNewsResponse(
                news.getId(),
                news.getTitle(),
                news.getContent(),
                news.getCategory()
        );
    }
}
