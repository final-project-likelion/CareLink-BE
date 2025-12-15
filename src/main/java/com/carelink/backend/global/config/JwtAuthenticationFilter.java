package com.carelink.backend.global.config;

import com.carelink.backend.global.exception.ErrorCode;
import com.carelink.backend.user.entity.User;
import com.carelink.backend.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = resolveToken(request);

        if (token != null) {
            try {
                // 토큰 파싱 + 유효성 체크
                Long id = jwtProvider.getUserIdFromToken(token);

                // 인증 성공 시
                User user = userRepository.findById(Long.valueOf(id))
                        .orElseThrow(() -> new BadCredentialsException("토큰 내 ID에 해당하는 사용자가 존재하지 않습니다."));

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (BadCredentialsException e) {
                request.setAttribute("exceptionCode", ErrorCode.INVALID_TOKEN);
                request.setAttribute("exceptionMessage", e.getMessage());

            } catch (CredentialsExpiredException e) {
                request.setAttribute("exceptionCode", ErrorCode.EXPIRED_TOKEN);
                request.setAttribute("exceptionMessage", e.getMessage());
            }
        } else {
            request.setAttribute("exceptionCode", ErrorCode.AUTH_REQUIRED);
            request.setAttribute("exceptionMessage", "토큰 인증이 필요합니다.");
        }
        filterChain.doFilter(request, response);
    }

    /** 토큰 파싱 **/
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
