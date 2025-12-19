package com.carelink.backend.training.news.ai;

import com.carelink.backend.training.news.ai.dto.SixWResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class AiSixWClient {

    private final RestTemplate restTemplate;

    @Value("${ai.sixw.url}")
    private String sixwUrl;

    public SixWResponseDto generateSixW(String title, String content) {

        SixWRequest request = new SixWRequest(title, content);

        return restTemplate.postForObject(
                sixwUrl,
                request,
                SixWResponseDto.class
        );
    }

    private record SixWRequest(String title, String content) {
    }
}
