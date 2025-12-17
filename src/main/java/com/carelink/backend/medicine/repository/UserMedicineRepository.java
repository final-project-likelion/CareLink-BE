package com.carelink.backend.medicine.repository;

import com.carelink.backend.medicine.entity.UserMedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMedicineRepository extends JpaRepository<UserMedicine, Long> {
}
