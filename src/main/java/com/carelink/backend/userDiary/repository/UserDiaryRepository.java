package com.carelink.backend.userDiary.repository;

import com.carelink.backend.userDiary.entity.UserDiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface UserDiaryRepository extends JpaRepository<UserDiary, Long> {
    Boolean existsByUserIdAndDate(Long userId, LocalDate date);
}
