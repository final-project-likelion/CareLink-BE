package com.carelink.backend.training.news.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class AiSummaryClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${AI_SUMMARY_URL}")
    private String aiSummaryUrl;

    public String generatePreviewSummary(String content) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = Map.of("content", content);
        HttpEntity<Map<String, String>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(aiSummaryUrl, request, Map.class);

        return (String) response.getBody().get("summary");
    }
}
