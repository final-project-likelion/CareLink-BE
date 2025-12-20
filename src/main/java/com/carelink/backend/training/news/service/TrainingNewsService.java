package com.carelink.backend.training.news.service;

import com.carelink.backend.training.news.dto.TrainingNewsResponse;
import com.carelink.backend.training.news.entity.News;
import com.carelink.backend.training.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TrainingNewsService {

    private final NewsRepository newsRepository;

    @Transactional(readOnly = true)
    public TrainingNewsResponse getTrainingNews(Long newsId) {

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
