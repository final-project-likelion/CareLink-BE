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
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),

    // STT 관련
    STT_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "음성 -> 텍스트 추출에 실패했습니다."),
    STT_TOO_LARGE(HttpStatus.BAD_REQUEST, "파일 용량/길이가 너무 큽니다."),

    // 외부 API (fastAPI ..) 관련
    EXTERNAL_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "채팅 응답 과정에서 오류가 발생했습니다."),

    // 채팅 관련
    CHAT_SAVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "채팅 저장 과정에서 오류가 발생했습니다."),
    JSON_MAPPING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "JSON 매핑에 실패했습니다."),
    CHAT_ROOM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "채팅방을 불러오는 과정에서 오류가 발생했습니다."),

    // 약 정보 관련
    USER_MEDICINE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 id의 약이 존재하지 않습니다."),
    MEDICINE_INTAKE_TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 id의 복용 시간이 존재하지 않습니다."),

    // 컨디션 관련
    USER_CONDITION_ALREADY_EXISTS(HttpStatus.CONFLICT, "해당 날짜의 컨디션 정보가 이미 존재합니다."),
    USER_CONDITION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 날짜의 컨디션 정보가 존재하지 않습니다."),

    // 이미지 관련
    IMAGE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다."),

    // 일기 관련
    USER_DIARY_ALREADY_EXISTS(HttpStatus.CONFLICT, "해당 날짜의 일기가 이미 존재합니다."),
    USER_DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 id의 일기가 존재하지 않습니다."),

    // 퀴즈 관련
    QUIZ_ALREADY_SOLVED(HttpStatus.FORBIDDEN, "오늘의 퀴즈는 이미 완료했습니다."),
    QUIZ_NOT_FOUND(HttpStatus.NOT_FOUND, "오늘의 퀴즈가 존재하지 않습니다."),

    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 입력값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
