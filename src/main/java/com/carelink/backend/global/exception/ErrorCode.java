package com.carelink.backend.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 인증 관련
    AUTH_REQUIRED(HttpStatus.UNAUTHORIZED,  "토큰 인증이 필요합니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED,  "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED,"만료된 토큰입니다."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND,  "토큰을 찾을 수 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다"),

    // 유저 관련
    DUPLICATED_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "이미 존재하는 전화번호입니다"),

    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 입력값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
