package com.carelink.backend.medicine.service;

import com.carelink.backend.medicine.dto.MedicineInfoDto;
import com.carelink.backend.medicine.entity.MedicineIntakeTime;
import com.carelink.backend.medicine.entity.UserMedicine;
import com.carelink.backend.medicine.repository.MedicineIntakeTimeRepository;
import com.carelink.backend.medicine.repository.UserMedicineRepository;
import com.carelink.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class MedicineService {

    private final UserMedicineRepository userMedicineRepository;
    private final MedicineIntakeTimeRepository medicineIntakeTimeRepository;

    /** 새로운 약 추가 */
    @Transactional
    public Long addMedicine(User user, MedicineInfoDto medicineInfoDto) {
        UserMedicine userMedicine = UserMedicine.builder()
                .user(user).name(medicineInfoDto.getName()).build();

        userMedicineRepository.save(userMedicine);

        for (LocalTime time : medicineInfoDto.getTimes()) {
            MedicineIntakeTime medicineIntakeTime = MedicineIntakeTime.builder()
                    .time(time)
                    .userMedicine(userMedicine).build();
            medicineIntakeTimeRepository.save(medicineIntakeTime);
        }

        return userMedicine.getId();
    }

}
