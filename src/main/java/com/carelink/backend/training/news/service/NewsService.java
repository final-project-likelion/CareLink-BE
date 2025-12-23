package com.carelink.backend.training.news.service;

import com.carelink.backend.training.news.dto.NewsListResponseDto;
import com.carelink.backend.training.news.dto.RecommendedNewsDto;
import com.carelink.backend.training.news.dto.GeneralNewsDto;
import com.carelink.backend.training.news.entity.News;
import com.carelink.backend.training.news.repository.CognitiveTrainingRepository;
import com.carelink.backend.training.news.repository.NewsRepository;
import com.carelink.backend.user.entity.User;
import com.carelink.backend.user.repository.UserRepository;
import com.carelink.backend.user.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsService {

    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final CognitiveTrainingRepository cognitiveTrainingRepository;


    public NewsListResponseDto getTodayNewsList(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        // 유저 관심 카테고리 추출
        List<Category> interestedCategories =
                user.getUserInterests().stream()
                        .map(com.carelink.backend.userInterest.entity.UserInterest::getCategory)
                        .toList();

        // 오늘 완료한 뉴스 ID 목록 조회
        Set<Long> completedNewsIds = new HashSet<>(
                cognitiveTrainingRepository.findCompletedNewsIds(userId)
        );

        // 최신 뉴스 8개 조회
        List<News> latestNews = newsRepository.findTop8ByOrderByCreatedDateDesc();

        List<RecommendedNewsDto> recommended = new ArrayList<>();
        List<GeneralNewsDto> others = new ArrayList<>();

        for (News news : latestNews) {

            int estimatedMinutes = estimateReadingTime(news.getContent());
            boolean completed = completedNewsIds.contains(news.getId());

            if (recommended.size() < 3 &&
                    interestedCategories.contains(news.getCategory())) {

                recommended.add(
                        new RecommendedNewsDto(
                                news.getId(),
                                news.getTitle(),
                                news.getThumbnailUrl(),
                                estimatedMinutes,
                                completed
                        )
                );
            } else {
                others.add(
                        new GeneralNewsDto(
                                news.getId(),
                                news.getTitle(),
                                news.getThumbnailUrl(),
                                estimatedMinutes,
                                news.getPreviewSummary(),
                                completed
                        )
                );
            }
        }

        return new NewsListResponseDto(recommended, others);
    }

    private int estimateReadingTime(String content) {
        int charCount = content.length();
        return Math.max(1, charCount / 500);
    }
}
