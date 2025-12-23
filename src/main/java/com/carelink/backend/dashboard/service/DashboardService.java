package com.carelink.backend.dashboard.service;

import com.carelink.backend.dashboard.dto.*;
import com.carelink.backend.quiz.repository.QuizAttemptRepository;
import com.carelink.backend.userCondition.repository.UserConditionRepository;
import com.carelink.backend.userDiary.repository.UserDiaryRepository;
import com.carelink.backend.training.news.repository.CognitiveTrainingRepository;
import com.carelink.backend.medicine.repository.MedicineIntakeLogRepository;
import com.carelink.backend.medicine.repository.MedicineIntakeTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

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

        // 최근 7일
        LocalDate last7Start = today.minusDays(6);

        // 주간 (월~일)
        LocalDate weekStart = today.with(DayOfWeek.MONDAY);
        LocalDate weekEnd = weekStart.plusDays(6);

        // 1. 컨디션
        List<ConditionDto> conditions =
                userConditionRepository
                        .findByUser_IdAndDateBetween(userId, last7Start, today)
                        .stream()
                        .map(c -> new ConditionDto(
                                c.getDate(),
                                c.getMoodScore()
                        ))
                        .toList();

        ConditionSectionDto conditionSection =
                new ConditionSectionDto(conditions);

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
        Integer todayQuizScore =
                quizAttemptRepository.findMaxScoreByUserIdAndSolvedDate(userId, today);

        String todayScore =
                todayQuizScore != null ? String.valueOf(todayQuizScore) : "-";

        List<DailyQuizScoreDto> quizScores =
                quizAttemptRepository
                        .findDailyMaxScores(userId, last7Start, today)
                        .stream()
                        .map(r -> new DailyQuizScoreDto(
                                (LocalDate) r[0],
                                (Integer) r[1]
                        ))
                        .toList();

        QuizSectionDto quizSection =
                new QuizSectionDto(todayScore, quizScores);

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
