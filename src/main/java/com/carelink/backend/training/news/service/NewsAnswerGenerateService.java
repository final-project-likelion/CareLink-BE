package com.carelink.backend.training.news.service;

import com.carelink.backend.training.news.ai.AiArticleSummaryClient;
import com.carelink.backend.training.news.ai.AiSixWClient;
import com.carelink.backend.training.news.ai.dto.ArticleSummaryResponseDto;
import com.carelink.backend.training.news.ai.dto.SixWResponseDto;
import com.carelink.backend.training.news.entity.ArticleSummaryAnswer;
import com.carelink.backend.training.news.entity.News;
import com.carelink.backend.training.news.entity.SixWAnswer;
import com.carelink.backend.training.news.repository.ArticleSummaryAnswerRepository;
import com.carelink.backend.training.news.repository.SixWAnswerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsAnswerGenerateService {

    private final SixWAnswerRepository sixWAnswerRepository;
    private final ArticleSummaryAnswerRepository articleSummaryAnswerRepository;
    private final AiSixWClient aiSixWClient;
    private final AiArticleSummaryClient aiArticleSummaryClient;

    /**
     * 육하원칙 + 기사 요약 생성
     * - 실패해도 News 저장에 영향 없음
     * - 트랜잭션 사용 안 함 (중요)
     */
    public void generateAnswers(News news) {

        // 1. 육하원칙
        try {
            SixWResponseDto sixw =
                    aiSixWClient.generateSixW(
                            news.getTitle(),
                            news.getContent()
                    );

            sixWAnswerRepository.save(
                    new SixWAnswer(
                            news,
                            sixw.getWho(),
                            sixw.getWhen(),
                            sixw.getWhere(),
                            sixw.getWhat(),
                            sixw.getWhy(),
                            sixw.getHow()
                    )
            );
        } catch (Exception e) {
            log.warn("육하원칙 생성 실패 - 무시", e);
        }

        // 2. 기사 요약
        try {
            ArticleSummaryResponseDto summary =
                    aiArticleSummaryClient.generateArticleSummary(
                            news.getTitle(),
                            news.getContent()
                    );

            articleSummaryAnswerRepository.save(
                    new ArticleSummaryAnswer(
                            news,
                            summary.getSummary()
                    )
            );
        } catch (Exception e) {
            log.warn("기사 요약 생성 실패 - 무시", e);
        }
    }
}
