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
                        .map(i -> i.getCategory())
                        .toList();

        // 이미 훈련한 뉴스
        Set<Long> trainedNewsIds =
                new HashSet<>(cognitiveTrainingRepository.findCompletedNewsIds(userId));

        boolean todayTrainingCompleted =
                cognitiveTrainingRepository
                        .existsByUserIdAndCompletedTrueAndCompletedDate(
                                userId, LocalDate.now()
                        );

        // 최근 3일치 뉴스
        LocalDate fromDate = LocalDate.now().minusDays(7);

        List<News> recentNews =
                newsRepository.findAllByOrderByCreatedDateDesc();


        // 1. 관심 카테고리 뉴스 우선 추출
        List<News> preferredNews =
                recentNews.stream()
                        .filter(news -> interestedCategories.contains(news.getCategory()))
                        .limit(3) // 추천 영역은 최대 3개
                        .toList();

        // 2. 나머지 뉴스로 채우기
        List<News> remainingNews =
                recentNews.stream()
                        .filter(news -> !preferredNews.contains(news))
                        .limit(8 - preferredNews.size())
                        .toList();

        // 3. 최종 리스트 (최대 8개)
        List<News> finalNewsList = new ArrayList<>();
        finalNewsList.addAll(preferredNews);
        finalNewsList.addAll(remainingNews);

        List<RecommendedNewsDto> recommended = new ArrayList<>();
        List<GeneralNewsDto> others = new ArrayList<>();

        for (News news : finalNewsList) {

            int estimatedMinutes = estimateReadingTime(news.getContent());
            boolean trained = trainedNewsIds.contains(news.getId());
            boolean canEnterTraining = !trained && !todayTrainingCompleted;

            // 추천 영역: "관심 카테고리 + 최대 3개"
            if (recommended.size() < 3 &&
                    interestedCategories.contains(news.getCategory())) {

                recommended.add(
                        new RecommendedNewsDto(
                                news.getId(),
                                news.getTitle(),
                                news.getThumbnailUrl(),
                                estimatedMinutes,
                                trained,
                                canEnterTraining
                        )
                );

            } else {
                // 나머지 영역
                others.add(
                        new GeneralNewsDto(
                                news.getId(),
                                news.getTitle(),
                                news.getThumbnailUrl(),
                                estimatedMinutes,
                                news.getPreviewSummary(),
                                trained,
                                canEnterTraining
                        )
                );
            }
        }

        return new NewsListResponseDto(recommended, others);
    }

    private int estimateReadingTime(String content) {
        int charCount = content.length();
        return Math.max(1, charCount / 480);
    }
}