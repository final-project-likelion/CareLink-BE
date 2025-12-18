package com.carelink.backend.user.service;

import com.carelink.backend.global.config.JwtProvider;
import com.carelink.backend.global.exception.BaseException;
import com.carelink.backend.global.exception.ErrorCode;
import com.carelink.backend.user.Category;
import com.carelink.backend.user.dto.LoginDto;
import com.carelink.backend.user.dto.SignUpDto;
import com.carelink.backend.user.dto.TokenDto;
import com.carelink.backend.user.entity.RefreshToken;
import com.carelink.backend.user.entity.User;
import com.carelink.backend.user.repository.RefreshTokenRepository;
import com.carelink.backend.user.repository.UserRepository;
import com.carelink.backend.userInterest.entity.UserInterest;
import com.carelink.backend.userInterest.repository.UserInterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserInterestRepository userInterestRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

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

    public TokenDto login(LoginDto loginDto) {
        User user = userRepository.findByPhoneNum(loginDto.getPhoneNum())
                .orElseThrow(() -> new BaseException(ErrorCode.UNAUTHORIZED, "잘못된 아이디 또는 비밀번호입니다"));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword()))
            throw new BaseException(ErrorCode.UNAUTHORIZED, "잘못된 아이디 또는 비밀번호입니다");
        else {
            TokenDto tokens = createAndSaveToken(user.getId());
            return tokens;
        }
    }

    /** access / refresh 토큰 생성 및 refresh token 저장 */
    @Transactional
    public TokenDto createAndSaveToken(Long userId) {
        TokenDto tokens = jwtProvider.createTokens(userId);

        refreshTokenRepository.findByUserId(userId)
                .ifPresentOrElse(
                        rt -> {
                            rt.updateRefreshToken(tokens.getRefreshToken());
                            refreshTokenRepository.save(rt);
                        }, () -> {
                            refreshTokenRepository.save(RefreshToken.builder().userId(userId).refreshToken(tokens.getRefreshToken()).build());
                        }
                );
        return tokens;
    }

    /** access token 만료 시 토큰 재발급 */
    public TokenDto reissue(String refreshToken) {
        Long userIdFromToken = jwtProvider.getUserIdFromToken(refreshToken);

        RefreshToken refreshtoken = refreshTokenRepository.findByUserId(userIdFromToken)
                .orElseThrow(() -> new BaseException(ErrorCode.TOKEN_NOT_FOUND, "해당 사용자 id의 refresh token을 찾을 수 없습니다."));

        if (!refreshtoken.getRefreshToken().equals(refreshToken))
            throw new BaseException(ErrorCode.INVALID_TOKEN, "refresh token 값이 올바르지 않습니다.");

        return createAndSaveToken(userIdFromToken);
    }

    /** 회원가입 시 id (전화번호) 중복 체크 */
    public Boolean isPhoneNumDuplicate(String phoneNum) {
        if (userRepository.existsByPhoneNum(phoneNum))
            return true;
        return false;
    }

}
