package com.carelink.backend.training.news.ai;

import com.carelink.backend.training.news.ai.dto.AiNewsFilterResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class AiNewsFilterClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${AI_NEWS_FILTER_URL}")
    private String aiNewsFilterUrl;

    public boolean isUsable(String title, String content) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = Map.of(
                "title", title,
                "content", content
        );

        HttpEntity<Map<String, String>> request =
                new HttpEntity<>(body, headers);

        try {
            ResponseEntity<AiNewsFilterResponse> response =
                    restTemplate.postForEntity(
                            aiNewsFilterUrl,
                            request,
                            AiNewsFilterResponse.class
                    );

            return response.getBody() != null && response.getBody().isUsable();

        } catch (Exception e) {
            // AI 서버 장애 or 타임아웃 시 → 무조건 안전하게 제외
            return false;
        }
    }
}
