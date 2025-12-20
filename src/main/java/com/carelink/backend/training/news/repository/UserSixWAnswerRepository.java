package com.carelink.backend.training.news.repository;

import com.carelink.backend.training.news.entity.News;
import com.carelink.backend.training.news.entity.UserSixWAnswer;
import com.carelink.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSixWAnswerRepository
        extends JpaRepository<UserSixWAnswer, Long> {

    Optional<UserSixWAnswer> findByUserAndNews(User user, News news);
}
