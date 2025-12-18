package com.carelink.backend.training.news.controller;

import com.carelink.backend.global.response.BaseResponse;
import com.carelink.backend.training.news.service.DailyNewsCrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/news")
@RequiredArgsConstructor
public class NewsCrawlingController {

    private final DailyNewsCrawlingService service;

    @PostMapping("/crawl")
    public BaseResponse<Void> crawlDailyNews() {
        service.crawlDailyNews();
        return BaseResponse.success("뉴스 크롤링이 완료되었습니다.", null);
    }
}
