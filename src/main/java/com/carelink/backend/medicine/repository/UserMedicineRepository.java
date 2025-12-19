package com.carelink.backend.medicine.repository;

import com.carelink.backend.medicine.entity.UserMedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMedicineRepository extends JpaRepository<UserMedicine, Long> {
    List<UserMedicine> findByUserId(Long userId);
    Optional<UserMedicine> findByUserIdAndId(Long userId, Long id);
}
