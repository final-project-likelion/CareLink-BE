package com.carelink.backend.quiz.config;

import com.carelink.backend.quiz.entity.Quiz;
import com.carelink.backend.quiz.repository.QuizRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class QuizInitDataLoader {

    private final QuizRepository quizRepository;

    @PostConstruct
    public void init() {
        // 이미 데이터가 있으면 DB에 또 추가하지 않음
        if (quizRepository.count() > 0) return;

        quizRepository.save(
                Quiz.builder()
                        .date(LocalDate.now())
                        .question("기억력 향상에 좋은 녹색 채소는 무엇인가요?")
                        .option1("당근")
                        .option2("시금치")
                        .option3("감자")
                        .option4("무")
                        .correctOption(2)
                        .build()
        );

        quizRepository.save(
                Quiz.builder()
                        .date(LocalDate.now().plusDays(1))
                        .question("1년은 몇 일일까요?")
                        .option1("365")
                        .option2("300")
                        .option3("370")
                        .option4("400")
                        .correctOption(1)
                        .build()
        );

        quizRepository.save(
                Quiz.builder()
                        .date(LocalDate.now().plusDays(2))
                        .question("세종대왕이 만든 글자는 무엇인가요?")
                        .option1("한자")
                        .option2("로마자")
                        .option3("일본어")
                        .option4("한글")
                        .correctOption(4)
                        .build()
        );

        quizRepository.save(
                Quiz.builder()
                        .date(LocalDate.now().plusDays(3))
                        .question("치매 예방에 좋은 운동은 무엇인가요?")
                        .option1("TV 시청")
                        .option2("늦게 자기")
                        .option3("걷기")
                        .option4("자동차 타기")
                        .correctOption(3)
                        .build()
        );
    }
}
