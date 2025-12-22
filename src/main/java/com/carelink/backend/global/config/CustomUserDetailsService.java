package com.carelink.backend.global.config;

import com.carelink.backend.user.entity.User;
import com.carelink.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNum) throws UsernameNotFoundException {
        User user = userRepository.findByPhoneNum(phoneNum)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다 - " + phoneNum));
        return new CustomUserDetails(user.getId());
    }

}
