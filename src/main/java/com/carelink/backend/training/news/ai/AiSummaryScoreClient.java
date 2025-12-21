package com.carelink.backend.training.news.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class AiSummaryScoreClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${AI_SUMMARY_SCORE_URL}")
    private String aiScoreUrl;

    public Integer scoreSummary(
            String article,
            String correctSummary,
            String userSummary
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = Map.of(
                "article", article,
                "correct_summary", correctSummary,
                "user_summary", userSummary
        );

        HttpEntity<Map<String, String>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(aiScoreUrl, request, Map.class);

        // 필수 방어 코드
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException(
                    "AI server error: " + response.getStatusCode()
            );
        }

        Map responseBody = response.getBody();
        if (responseBody == null) {
            throw new IllegalStateException("AI response body is null");
        }

        Object score = responseBody.get("score");
        return ((Number) score).intValue();
    }
}
