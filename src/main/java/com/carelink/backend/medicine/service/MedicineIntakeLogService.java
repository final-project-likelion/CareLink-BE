package com.carelink.backend.medicine.service;

import com.carelink.backend.global.exception.BaseException;
import com.carelink.backend.global.exception.ErrorCode;
import com.carelink.backend.medicine.dto.DailyMedicineIntakeCheckDto;
import com.carelink.backend.medicine.dto.DailyMedicineLogDto;
import com.carelink.backend.medicine.entity.MedicineIntakeLog;
import com.carelink.backend.medicine.entity.MedicineIntakeTime;
import com.carelink.backend.medicine.repository.MedicineIntakeLogRepository;
import com.carelink.backend.medicine.repository.MedicineIntakeTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicineIntakeLogService {

    private final MedicineIntakeTimeRepository medicineIntakeTimeRepository;
    private final MedicineIntakeLogRepository medicineIntakeLogRepository;

    /** 오늘 복용 약 리스트 + 복용 여부 조회 */
    public DailyMedicineLogDto getDailyMedicineLog(Long userId) {

        // 1. 전체 약 복용 시간 조회
        List<MedicineIntakeTime> medicineIntakeTimes = medicineIntakeTimeRepository.findByUserMedicine_User_Id(userId);

        // 2. 복용 시간별 복용 기록 조회
        List<DailyMedicineLogDto.DailyMedicineInfoDto> moriningMedicines = new ArrayList<>();
        List<DailyMedicineLogDto.DailyMedicineInfoDto> noonMedicines = new ArrayList<>();
        List<DailyMedicineLogDto.DailyMedicineInfoDto> eveningMedicines = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        for (MedicineIntakeTime medicineIntakeTime : medicineIntakeTimes) {
            if (medicineIntakeLogRepository.existsByMedicineIntakeTimeIdAndDate(medicineIntakeTime.getId(), LocalDate.now())) {
                // 2-1. DailyMedicineInfoDto 생성
                DailyMedicineLogDto.DailyMedicineInfoDto dailyMedicineInfoDto = getDailyMedicineInfoDto(medicineIntakeTime, true, formatter);

                // 2-2. 복용 시간 분리해서 리스트에 넣기
                classifyTime(medicineIntakeTime, moriningMedicines, dailyMedicineInfoDto, noonMedicines, eveningMedicines);
            } else { // 약 복용 기록이 없는 경우
                DailyMedicineLogDto.DailyMedicineInfoDto dailyMedicineInfoDto = getDailyMedicineInfoDto(medicineIntakeTime, false, formatter);

                classifyTime(medicineIntakeTime, moriningMedicines, dailyMedicineInfoDto, noonMedicines, eveningMedicines);
            }

        }

        // 3. 복용 시간 순 정렬
        moriningMedicines.sort(Comparator.comparing(dto -> LocalTime.parse(dto.getTime(), formatter)));
        noonMedicines.sort(Comparator.comparing(dto -> LocalTime.parse(dto.getTime(), formatter)));
        eveningMedicines.sort(Comparator.comparing(dto -> LocalTime.parse(dto.getTime(), formatter)));

        // 4. DailyMedicineLogDto 반환
        return DailyMedicineLogDto.builder()
                .morningMedicines(moriningMedicines)
                .noonMedicines(noonMedicines)
                .eveningMedicines(eveningMedicines).build();

    }

    /** 오늘 약 복용 기록 업데이트 */
    @Transactional
    public void checkDailyMedicineIntake(Long userId, DailyMedicineIntakeCheckDto dailyMedicineIntakeCheckDto) {
        // 1. 오늘 기존 약 복용 기록 MedicineIntakeTimeId 조회
        List<MedicineIntakeLog> existingLogs = medicineIntakeLogRepository.findByMedicineIntakeTime_UserMedicine_User_IdAndDate(userId, LocalDate.now());
        Set<Long> existingLogIds = existingLogs.stream()
                .map(log -> log.getMedicineIntakeTime().getId())
                .collect(Collectors.toSet());

        // 2. 새로운 약 복용 기록 MedicineIntakeTimeId Set
        Set<Long> requestLogIds = new HashSet<>(dailyMedicineIntakeCheckDto.getMedicineIntakeTimeIds());

        // 3. 새로 생성할 복용 기록 (DB에는 X, 요청에는 O)
        Set<Long> createIds = new HashSet<>(requestLogIds);
        createIds.removeAll(existingLogIds);

        // 4. 삭제할 복용 기록 (DB에는 O, 요청에는 X - 체크 취소한 경우)
        Set<Long> deleteIds = new HashSet<>(existingLogIds);
        deleteIds.removeAll(requestLogIds);

        // 5. DailyMedicineLog 생성
        for (Long medicineIntakeTimeId : createIds) {
            MedicineIntakeTime medicineIntakeTime = medicineIntakeTimeRepository.findByUserMedicine_User_IdAndId(userId, medicineIntakeTimeId)
                    .orElseThrow(() -> new BaseException(ErrorCode.MEDICINE_INTAKE_TIME_NOT_FOUND));

            MedicineIntakeLog medicineIntakeLog = MedicineIntakeLog.builder()
                    .medicineIntakeTime(medicineIntakeTime)
                    .date(LocalDate.now()).build();

            medicineIntakeLogRepository.save(medicineIntakeLog);
        }

        // 6. DailyMedicineLog 삭제
        if (!deleteIds.isEmpty()) {
            medicineIntakeLogRepository.deleteByMedicineIntakeTimeIdInAndDate(deleteIds, LocalDate.now());
        }
    }



    /** DailyMedicineInfoDto 생성 메서드 */
    private DailyMedicineLogDto.DailyMedicineInfoDto getDailyMedicineInfoDto(MedicineIntakeTime medicineIntakeTime, Boolean isTaken, DateTimeFormatter formatter) {
        DailyMedicineLogDto.DailyMedicineInfoDto dailyMedicineInfoDto = DailyMedicineLogDto.DailyMedicineInfoDto.builder()
                .id(medicineIntakeTime.getId())
                .name(medicineIntakeTime.getUserMedicine().getName())
                .time(medicineIntakeTime.getTime().format(formatter))
                .isTaken(isTaken).build();
        return dailyMedicineInfoDto;
    }

    /** 아침, 점심, 저녁 약 구분 메서드 */
    private void classifyTime(MedicineIntakeTime medicineIntakeTime, List<DailyMedicineLogDto.DailyMedicineInfoDto> moriningMedicines, DailyMedicineLogDto.DailyMedicineInfoDto dailyMedicineInfoDto, List<DailyMedicineLogDto.DailyMedicineInfoDto> noonMedicines, List<DailyMedicineLogDto.DailyMedicineInfoDto> eveningMedicines) {
        LocalTime time = medicineIntakeTime.getTime();
        if (time.isBefore(LocalTime.of(11, 0)))
            moriningMedicines.add(dailyMedicineInfoDto);
        else if (time.isBefore(LocalTime.of(17, 0)))
            noonMedicines.add(dailyMedicineInfoDto);
        else eveningMedicines.add(dailyMedicineInfoDto);
    }

}
