package com.carelink.backend.training.news.controller;

import com.carelink.backend.training.news.dto.*;
import com.carelink.backend.training.news.service.TrainingQueryService;
import com.carelink.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @AuthenticationPrincipal User user,
            @RequestParam String month
    ) {
        return trainingQueryService.getMonthlyTrainings(
                user.getId(),
                YearMonth.parse(month)
        );
    }


    @GetMapping("/{newsId}")
    public TrainingDetailResponse getTrainingDetail(
            @AuthenticationPrincipal User user,
            @PathVariable Long newsId
    ) {
        return trainingQueryService.getTrainingDetail(
                user.getId(),
                newsId
        );
    }

}
