package com.carelink.backend.training.news.ai;

import com.carelink.backend.training.news.ai.dto.ArticleSummaryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class AiArticleSummaryClient {

    private final RestTemplate restTemplate;

    @Value("${ai.article-summary.url}")
    private String articleSummaryUrl;

    public ArticleSummaryResponseDto generateArticleSummary(
            String title,
            String content
    ) {
        ArticleSummaryRequest request =
                new ArticleSummaryRequest(title, content);

        return restTemplate.postForObject(
                articleSummaryUrl,
                request,
                ArticleSummaryResponseDto.class
        );
    }

    private record ArticleSummaryRequest(
            String title,
            String content
    ) {
    }
}
