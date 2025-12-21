package com.carelink.backend.training.news.controller;

import com.carelink.backend.global.response.BaseResponse;
import com.carelink.backend.training.news.service.DailyNewsCrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/news")
@RequiredArgsConstructor
public class NewsAdminController {

    private final DailyNewsCrawlingService dailyNewsCrawlingService;

    @PostMapping("/prepare")
    public BaseResponse<Void> prepareDailyNews() {
        dailyNewsCrawlingService.crawlDailyNews();
        return BaseResponse.success("뉴스 크롤링 + AI 요약 완료", null);
    }
}
