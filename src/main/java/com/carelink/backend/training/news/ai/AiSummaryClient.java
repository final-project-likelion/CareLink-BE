package com.carelink.backend.training.news.ai;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class AiSummaryClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String AI_SUMMARY_URL =
            "http://localhost:8000/internal/preview-summary";

    public String generatePreviewSummary(String content) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = Map.of("content", content);
        HttpEntity<Map<String, String>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(AI_SUMMARY_URL, request, Map.class);

        return (String) response.getBody().get("summary");
    }
}
