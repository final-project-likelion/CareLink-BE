package com.carelink.backend.training.news.service;

import com.carelink.backend.training.news.dto.CorrectSixWAnswer;
import com.carelink.backend.training.news.dto.SixWResultResponse;
import com.carelink.backend.training.news.dto.UserSixWSubmitRequest;
import com.carelink.backend.training.news.entity.News;
import com.carelink.backend.training.news.entity.SixWAnswer;
import com.carelink.backend.training.news.entity.UserSixWAnswer;
import com.carelink.backend.training.news.repository.NewsRepository;
import com.carelink.backend.training.news.repository.SixWAnswerRepository;
import com.carelink.backend.training.news.repository.UserSixWAnswerRepository;
import com.carelink.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SixWService {

    private final NewsRepository newsRepository;
    private final SixWAnswerRepository sixWAnswerRepository;
    private final UserSixWAnswerRepository userSixWAnswerRepository;

    public SixWResultResponse submitSixWAnswer(
            User user,
            Long newsId,
            UserSixWSubmitRequest request
    ) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new IllegalArgumentException("뉴스 없음"));

        // ✅ (체크) 수정: 사용자 육하원칙 답변 저장 로직은 유지
        UserSixWAnswer userAnswer = new UserSixWAnswer(
                user,
                news,
                request.who(),
                request.whenAt(),
                request.whereAt(),
                request.what(),
                request.why(),
                request.how()
        );
        userSixWAnswerRepository.save(userAnswer);

        // ✅ (체크) 수정: 정답만 조회
        SixWAnswer correct = sixWAnswerRepository.findByNews(news)
                .orElseThrow(() -> new IllegalStateException("정답 없음"));

        // ✅ (체크) 수정: CorrectSixWAnswer DTO로 변환
        CorrectSixWAnswer correctAnswer = new CorrectSixWAnswer(
                correct.getWho(),
                correct.getWhenAt(),
                correct.getWhereAt(),
                correct.getWhat(),
                correct.getWhy(),
                correct.getHow()
        );

        // ✅ (체크) 수정: newsId 포함 + userAnswer 제거
        return new SixWResultResponse(
                news.getId(),
                correctAnswer,
                "육하원칙 제출완료"
        );
    }
}
