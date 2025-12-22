package com.carelink.backend.training.news.controller;

import com.carelink.backend.global.config.CustomUserDetails;
import com.carelink.backend.global.response.BaseResponse;
import com.carelink.backend.training.news.dto.*;
import com.carelink.backend.training.news.service.TrainingQueryService;
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
    public BaseResponse<List<MonthlyTrainingResponse>> getMonthlyTrainings(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam String month
    ) {
        return BaseResponse.success(
                trainingQueryService.getMonthlyTrainings(
                        customUserDetails.getId(),
                        YearMonth.parse(month)
                )
        );
    }


    @GetMapping("/{newsId}")
    public BaseResponse<TrainingDetailResponse> getTrainingDetail(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long newsId
    ) {
        return BaseResponse.success(
                trainingQueryService.getTrainingDetail(
                        customUserDetails.getId(),
                        newsId
                )
        );
    }


}
