package com.carelink.backend.training.news.controller;

import com.carelink.backend.global.response.BaseResponse;
import com.carelink.backend.training.news.dto.TrainingNewsResponse;
import com.carelink.backend.training.news.service.TrainingNewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trainings/news")
@RequiredArgsConstructor
public class TrainingNewsController {

    private final TrainingNewsService trainingNewsService;

    // 인지훈련 진입용 뉴스 상세 조회
    @GetMapping("/{newsId}")
    public BaseResponse<TrainingNewsResponse> getTrainingNews(
            @PathVariable Long newsId
    ) {
        TrainingNewsResponse response =
                trainingNewsService.getTrainingNews(newsId);

        return BaseResponse.success(
                "인지훈련용 뉴스 조회가 완료되었습니다.",
                response
        );
    }
}
