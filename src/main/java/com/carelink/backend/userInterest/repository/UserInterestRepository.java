package com.carelink.backend.userInterest.repository;

import com.carelink.backend.userInterest.entity.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {
}
