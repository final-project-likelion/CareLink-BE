package com.carelink.backend.training.news.controller;

import com.carelink.backend.global.response.BaseResponse;
import com.carelink.backend.training.news.dto.NewsListResponseDto;
import com.carelink.backend.training.news.service.NewsService;
import com.carelink.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trainings/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    public BaseResponse<NewsListResponseDto> getNewsList(
            @AuthenticationPrincipal User user
    ) {
        return BaseResponse.success(
                "뉴스 목록 조회 성공",
                newsService.getTodayNewsList(user.getId())
        );
    }
}
