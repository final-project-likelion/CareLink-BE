package com.carelink.backend.medicine.repository;

import com.carelink.backend.medicine.entity.MedicineIntakeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Repository
public interface MedicineIntakeLogRepository extends JpaRepository<MedicineIntakeLog, Long> {
    Boolean existsByMedicineIntakeTimeIdAndDate(Long medcineIntakeTimeId, LocalDate date);
    List<MedicineIntakeLog> findByMedicineIntakeTime_UserMedicine_User_IdAndDate(Long userId, LocalDate date);
    void deleteByMedicineIntakeTimeIdInAndDate(Set<Long> medicineIntakeTimeId, LocalDate date);
    void deleteByIdIn(List<Long> ids);
    List<MedicineIntakeLog> findByMedicineIntakeTimeIdIn(List<Long> medicineIntakeTimeId);
    Boolean existsByMedicineIntakeTime_UserMedicine_User_IdAndDate(Long userId, LocalDate date);

    // 오늘 복용한 총 횟수 (복용 기록이 없으면 0 반환)
    @Query("""
    SELECT COALESCE(COUNT(l), 0)
    FROM MedicineIntakeLog l
    JOIN l.medicineIntakeTime t
    JOIN t.userMedicine m
    WHERE m.user.id = :userId
      AND l.date = :date
    """)
    Long countByUserIdAndDate(Long userId, LocalDate date);

}
