package com.carelink.backend.training.news.controller;

import com.carelink.backend.training.news.service.DailyNewsCrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/news")
@RequiredArgsConstructor
public class NewsAdminController {

    private final DailyNewsCrawlingService dailyNewsCrawlingService;

    @PostMapping("/prepare")
    public String prepareDailyNews() {
        dailyNewsCrawlingService.crawlDailyNews();
        return "뉴스 크롤링 + AI 요약 완료";
    }
}
