package com.carelink.backend.training.news.ai;

import com.carelink.backend.training.news.ai.dto.SummaryScoreRequest;
import com.carelink.backend.training.news.ai.dto.SummaryScoreResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AiSummaryScoreClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${AI_SUMMARY_SCORE_URL}")
    private String aiSummaryScoreUrl;

    public Integer scoreSummary(
            String article,
            String correctSummary,
            String userSummary
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        SummaryScoreRequest body = new SummaryScoreRequest(
                article,
                correctSummary,
                userSummary
        );

        HttpEntity<SummaryScoreRequest> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<SummaryScoreResponse> response =
                restTemplate.postForEntity(
                        aiSummaryScoreUrl,
                        request,
                        SummaryScoreResponse.class
                );

        return response.getBody().score();
    }
}
