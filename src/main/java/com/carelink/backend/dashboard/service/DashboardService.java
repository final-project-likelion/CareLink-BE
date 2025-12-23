package com.carelink.backend.dashboard.service;

import com.carelink.backend.dashboard.dto.*;
import com.carelink.backend.quiz.entity.QuizAttempt;
import com.carelink.backend.quiz.repository.QuizAttemptRepository;
import com.carelink.backend.userCondition.entity.UserCondition;
import com.carelink.backend.userCondition.repository.UserConditionRepository;
import com.carelink.backend.userDiary.repository.UserDiaryRepository;
import com.carelink.backend.training.news.repository.CognitiveTrainingRepository;
import com.carelink.backend.medicine.repository.MedicineIntakeLogRepository;
import com.carelink.backend.medicine.repository.MedicineIntakeTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserConditionRepository userConditionRepository;
    private final UserDiaryRepository userDiaryRepository;
    private final CognitiveTrainingRepository cognitiveTrainingRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final MedicineIntakeLogRepository medicineIntakeLogRepository;
    private final MedicineIntakeTimeRepository medicineIntakeTimeRepository;

    public DashboardResponseDto getDashboard(Long userId) {

        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(6);
        // 최근 7일
        LocalDate last7Start = today.minusDays(6);

        // 주간 (월~일)
        LocalDate weekStart = today.with(DayOfWeek.MONDAY);
        LocalDate weekEnd = weekStart.plusDays(6);

        // 1. 컨디션
        Map<LocalDate, Integer> moodMap =
                userConditionRepository
                        .findByUser_IdAndDateBetween(userId, start, today)
                        .stream()
                        .collect(Collectors.toMap(
                                UserCondition::getDate,
                                UserCondition::getMoodScore
                        ));

        List<Integer> moodScores = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = start.plusDays(i);
            moodScores.add(moodMap.getOrDefault(date, 0));
            // 컨디션 데이터 없는 경우 0으로 채우긴 하되, 더미데이터에는 데이터가 매일 입력된 것으로 가정합시다!
        }

        // 상태 계산 로직
        double average =
                moodScores.stream()
                        .mapToInt(Integer::intValue)
                        .average()
                        .orElse(0.0);

        String status;
        if (average >= 1.2) {
            status = "좋음";
        } else if (average >= 0.6) {
            status = "보통";
        } else {
            status = "나쁨";
        }


        ConditionSectionDto conditionSection =
                new ConditionSectionDto(status, moodScores);


        // 2. 일기
        WeeklyStatusDto diaryStatus =
                buildWeeklyStatus(
                        userDiaryRepository.existsByUserIdAndDate(userId, today),
                        userDiaryRepository.countByUser_IdAndDateBetween(
                                userId, weekStart, weekEnd
                        )
                );

        // 3. 인지 훈련
        WeeklyStatusDto cognitiveStatus =
                buildWeeklyStatus(
                        cognitiveTrainingRepository
                                .existsByUserIdAndCompletedDate(userId, today),
                        cognitiveTrainingRepository
                                .countByUser_IdAndCompletedDateBetween(
                                        userId, weekStart, weekEnd
                                )
                );

        // 4. 약 복용
        WeeklyStatusDto medicineStatus =
                buildMedicineStatus(userId, today, weekStart, weekEnd);

        // 5. 퀴즈
        // 오늘 퀴즈 시도 조회
        Optional<QuizAttempt> todayAttempt =
                quizAttemptRepository.findByUserIdAndSolvedDate(userId, today);

        // 서버 내부용 점수 (항상 int)
        int todayScoreValue =
                todayAttempt.map(QuizAttempt::getScore)
                        .orElse(0);

        // 응답용 표시 값 (여기서만 "-")
        String todayScore =
                todayAttempt.map(a -> String.valueOf(a.getScore()))
                        .orElse("-");

        // 최근 7일 퀴즈 기록 조회
        List<QuizAttempt> attempts =
                quizAttemptRepository.findByUserIdAndSolvedDateBetween(userId, start, today);

        // 날짜 → 점수 맵
        Map<LocalDate, Integer> scoreMap =
                attempts.stream()
                        .collect(Collectors.toMap(
                                QuizAttempt::getSolvedDate,
                                QuizAttempt::getScore
                        ));

        // 7일 점수 배열 (과거 → 최신)
        List<Integer> scores = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = start.plusDays(i);
            scores.add(scoreMap.getOrDefault(date, 0)); // 안 푼 날 = 0점
        }

        // DTO 조립
        QuizSectionDto quizSection =
                new QuizSectionDto(todayScore, scores);

        return new DashboardResponseDto(
                conditionSection,
                medicineStatus,
                diaryStatus,
                cognitiveStatus,
                quizSection
        );
    }

    private WeeklyStatusDto buildWeeklyStatus(
            boolean todayDone,
            int completedDays
    ) {
        return new WeeklyStatusDto(
                todayDone ? "완료" : "작성 전",
                completedDays,
                (int) ((completedDays / 7.0) * 100)
        );
    }

    private WeeklyStatusDto buildMedicineStatus(
            Long userId,
            LocalDate today,
            LocalDate weekStart,
            LocalDate weekEnd
    ) {
        int totalToday =
                medicineIntakeTimeRepository.countByUserId(userId);
        // 오늘 실제 복용한 횟수
        Long doneToday =
                medicineIntakeLogRepository.countByUserIdAndDate(userId, today);

        int safeDoneToday = doneToday.intValue();

        boolean todayDone =
                totalToday > 0 && totalToday == safeDoneToday;

        // 주간 완료 일수 (하루에 모든 복용을 완료한 날짜 수)
        int completedDays = 0;

        LocalDate cursor = weekStart;
        while (!cursor.isAfter(weekEnd)) {

            int doneCount =
                    medicineIntakeLogRepository
                            .findByMedicineIntakeTime_UserMedicine_User_IdAndDate(userId, cursor)
                            .size();

            if (totalToday > 0 && doneCount == totalToday) {
                completedDays++;
            }

            cursor = cursor.plusDays(1);
        }

        return new WeeklyStatusDto(
                todayDone ? "완료" : "작성 전",
                completedDays,
                (int) ((completedDays / 7.0) * 100)
        );

    }
}
