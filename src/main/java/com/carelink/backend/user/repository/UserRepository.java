package com.carelink.backend.user.repository;

import com.carelink.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByPhoneNum(String phoneNum);
}