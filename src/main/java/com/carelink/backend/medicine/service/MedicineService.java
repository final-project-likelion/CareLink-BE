package com.carelink.backend.medicine.service;

import com.carelink.backend.global.exception.BaseException;
import com.carelink.backend.global.exception.ErrorCode;
import com.carelink.backend.medicine.dto.MedicineInfoDto;
import com.carelink.backend.medicine.dto.MedicineUpsertRequestDto;
import com.carelink.backend.medicine.entity.MedicineIntakeLog;
import com.carelink.backend.medicine.entity.MedicineIntakeTime;
import com.carelink.backend.medicine.entity.UserMedicine;
import com.carelink.backend.medicine.repository.MedicineIntakeLogRepository;
import com.carelink.backend.medicine.repository.MedicineIntakeTimeRepository;
import com.carelink.backend.medicine.repository.UserMedicineRepository;
import com.carelink.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicineService {

    private final UserMedicineRepository userMedicineRepository;
    private final MedicineIntakeTimeRepository medicineIntakeTimeRepository;
    private final MedicineIntakeLogRepository medicineIntakeLogRepository;

    /** 새로운 약 추가 */
    @Transactional
    public Long addMedicine(User user, MedicineUpsertRequestDto medicineUpsertRequestDto) {
        UserMedicine userMedicine = UserMedicine.builder()
                .user(user).name(medicineUpsertRequestDto.getName()).build();

        userMedicineRepository.save(userMedicine);

        for (LocalTime time : medicineUpsertRequestDto.getTimes()) {
            MedicineIntakeTime medicineIntakeTime = MedicineIntakeTime.builder()
                    .time(time)
                    .userMedicine(userMedicine).build();
            medicineIntakeTimeRepository.save(medicineIntakeTime);
        }

        return userMedicine.getId();
    }

    /** 약 목록 조회 */
    public List<MedicineInfoDto> getMedicineInfoByUserId(Long userId) {
        List<MedicineInfoDto> medicineInfoDtos = new ArrayList<>();

        // 사용자 id에 해당하는 UserMedicine 객체 조회
        List<UserMedicine> userMedicines = userMedicineRepository.findByUserId(userId);
        if (userMedicines.isEmpty()) return List.of();

        // UserMedicine별 MedicineIntakeTime 조회
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        for (UserMedicine userMedicine : userMedicines) {
            // 약 복용 시간 조회
            List<MedicineIntakeTime> medicineIntakeTimes = medicineIntakeTimeRepository.findByUserMedicineId(userMedicine.getId());

            // MedicineIntakeTimeDto (id + time) 생성
            List<MedicineInfoDto.MedicineIntakeTimeDto> medicineIntakeTimeDtos = new ArrayList<>();
            for (MedicineIntakeTime medicineIntakeTime : medicineIntakeTimes) {
                MedicineInfoDto.MedicineIntakeTimeDto time = MedicineInfoDto.MedicineIntakeTimeDto.builder()
                        .id(medicineIntakeTime.getId())
                        .time(medicineIntakeTime.getTime().format(formatter)).build();
                medicineIntakeTimeDtos.add(time);
            }

            MedicineInfoDto dto = MedicineInfoDto.builder()
                    .id(userMedicine.getId())
                    .name(userMedicine.getName())
                    .times(medicineIntakeTimeDtos).build();
            medicineInfoDtos.add(dto);
        }

        return medicineInfoDtos;
    }

    /** 약 삭제 */
    @Transactional
    public void deleteMedicine(Long userId, Long medicineId) {
        // 해당 id의 약 조회
        UserMedicine userMedicine = userMedicineRepository.findByUserIdAndId(userId, medicineId)
                .orElseThrow(() -> new BaseException(ErrorCode.USER_MEDICINE_NOT_FOUND));

        // 해당 약의 복용 시간 조회
        List<Long> medicineIntakeTimeIds = medicineIntakeTimeRepository.findByUserMedicineId(userMedicine.getId()).stream()
                .map(MedicineIntakeTime::getId)
                .toList();

        // 해당 약의 복용 기록 조회
        if (!medicineIntakeTimeIds.isEmpty()) {
            List<Long> medicineIntakeLogIds = medicineIntakeLogRepository.findByMedicineIntakeTimeIdIn(medicineIntakeTimeIds)
                    .stream().map(MedicineIntakeLog::getId).toList();

            if (!medicineIntakeLogIds.isEmpty())
                medicineIntakeLogRepository.deleteByIdIn(medicineIntakeLogIds);

            medicineIntakeTimeRepository.deleteByIdIn(medicineIntakeTimeIds);
        }

        userMedicineRepository.delete(userMedicine);
    }

}
