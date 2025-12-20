package com.carelink.backend.userDiary.repository;

import com.carelink.backend.userDiary.entity.UserDiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface UserDiaryRepository extends JpaRepository<UserDiary, Long> {
    Boolean existsByUserIdAndDate(Long userId, LocalDate date);

    @Query("""
        SELECT ud
        FROM UserDiary ud
        WHERE ud.date >= :start AND ud.date < :end AND ud.user.id = :userId
        ORDER BY ud.date ASC
    """)
    List<UserDiary> findByDateBetweenAndUserId(@Param("userId") Long userId, @Param("start") LocalDate start, @Param("end") LocalDate end);
}
