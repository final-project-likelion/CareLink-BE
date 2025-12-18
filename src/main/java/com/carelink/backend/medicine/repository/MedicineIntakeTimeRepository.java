package com.carelink.backend.medicine.repository;

import com.carelink.backend.medicine.entity.MedicineIntakeTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineIntakeTimeRepository extends JpaRepository<MedicineIntakeTime, Long> {
    List<MedicineIntakeTime> findByUserMedicineId(Long userMedicineId);
    List<MedicineIntakeTime> findByUserMedicine_User_Id(Long userId);
    Optional<MedicineIntakeTime> findByUserMedicine_User_IdAndId(Long userId, Long medicineIntakeTimeId);
    void deleteByIdIn(List<Long> ids);
}
