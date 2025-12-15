package com.carelink.backend.user.service;

import com.carelink.backend.global.exception.BaseException;
import com.carelink.backend.global.exception.ErrorCode;
import com.carelink.backend.user.Category;
import com.carelink.backend.user.dto.SignUpDto;
import com.carelink.backend.user.entity.User;
import com.carelink.backend.user.repository.UserRepository;
import com.carelink.backend.userInterest.entity.UserInterest;
import com.carelink.backend.userInterest.repository.UserInterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserInterestRepository userInterestRepository;

    @Transactional
    public Long signUp(SignUpDto signUpDto) {

        // 1. 전화번호 중복 확인
        String phoneNum = signUpDto.getPhoneNum();
        if (userRepository.existsByPhoneNum(phoneNum))
            throw new BaseException(ErrorCode.DUPLICATED_PHONE_NUMBER);

        // 2. User 객체 생성 및 저장
        User user = User.builder().name(signUpDto.getName())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .phoneNum(phoneNum)
                .birthday(signUpDto.getBirthday())
                .cognitiveState(signUpDto.getCognitiveState())
                .caregiverName(signUpDto.getCaregiverName())
                .caregiverPhoneNum(signUpDto.getCaregiverPhoneNum())
                .caregiverEmail(signUpDto.getCaregiverEmail()).build();

        User savedUser = userRepository.save(user);

        // 3. User 관심 카테고리 객체 생성 및 저장
        for (Category category : signUpDto.getInterestedCategory()) {
            UserInterest userInterest = UserInterest.builder().category(category)
                    .user(savedUser).build();
            userInterestRepository.save(userInterest);
        }

        return savedUser.getId();

    }

}
