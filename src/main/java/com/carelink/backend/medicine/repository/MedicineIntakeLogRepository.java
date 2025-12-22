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

    int countByUserIdAndDate(Long userId, LocalDate date);

    @Query("""
SELECT COUNT(DISTINCT l.date)
FROM MedicineIntakeLog l
JOIN l.medicineIntakeTime t
JOIN t.userMedicine m
WHERE m.user.id = :userId
  AND l.date BETWEEN :start AND :end
GROUP BY l.date
HAVING COUNT(l.id) =
      (SELECT COUNT(t2.id)
       FROM MedicineIntakeTime t2
       JOIN t2.userMedicine m2
       WHERE m2.user.id = :userId)
""")
    int countFullyCompletedDays(
            Long userId,
            LocalDate start,
            LocalDate end
    );

}
