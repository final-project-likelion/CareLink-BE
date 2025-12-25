package com.carelink.backend.training.news.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecommendedNewsDto {

    private Long newsId;
    private String title;
    private String thumbnailUrl;
    private int estimatedMinutes;
    private boolean trained; // 과거에 이 기사로 훈련한 적 있는지 여부 반환 (훈련한 적 있으면 기사 클릭 못함)
    private boolean canEnterTraining; // 클릭 가능 여부 반환 (오늘 훈련 완료했으면 무조건 false임)
}
