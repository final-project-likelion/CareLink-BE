package com.carelink.backend.medicine.repository;

import com.carelink.backend.medicine.entity.MedicineIntakeTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineIntakeTimeRepository extends JpaRepository<MedicineIntakeTime, Integer> {
    List<MedicineIntakeTime> findByUserMedicineId(Long userMedicineId);
}
