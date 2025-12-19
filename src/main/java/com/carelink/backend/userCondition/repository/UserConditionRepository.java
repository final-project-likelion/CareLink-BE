package com.carelink.backend.userCondition.repository;

import com.carelink.backend.userCondition.entity.UserCondition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface UserConditionRepository extends JpaRepository<UserCondition, Long> {
    Optional<UserCondition> findByUserIdAndDate(Long userId, LocalDate date);
    Boolean existsByUserIdAndDate(Long userId, LocalDate date);
}
