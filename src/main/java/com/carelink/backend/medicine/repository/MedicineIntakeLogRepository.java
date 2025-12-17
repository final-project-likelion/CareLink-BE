package com.carelink.backend.medicine.repository;

import com.carelink.backend.medicine.entity.MedicineIntakeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineIntakeLogRepository extends JpaRepository<MedicineIntakeLog, Integer> {
}
