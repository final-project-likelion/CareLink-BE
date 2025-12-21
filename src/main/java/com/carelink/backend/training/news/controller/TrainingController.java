package com.carelink.backend.training.news.controller;

import com.carelink.backend.training.news.dto.*;
import com.carelink.backend.training.news.service.TrainingQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trainings")
public class TrainingController {

    private final TrainingQueryService trainingQueryService;

    @GetMapping("/monthly")
    public List<MonthlyTrainingResponse> getMonthlyTrainings(
            @RequestParam Long userId,
            @RequestParam String month // yyyy-MM
    ) {
        return trainingQueryService.getMonthlyTrainings(
                userId,
                YearMonth.parse(month)
        );
    }

    @GetMapping("/{newsId}")
    public TrainingDetailResponse getTrainingDetail(
            @RequestParam Long userId,
            @PathVariable Long newsId
    ) {
        return trainingQueryService.getTrainingDetail(userId, newsId);
    }
}
