package com.carelink.backend.global.exception;

import com.carelink.backend.global.response.BaseResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 커스텀 에러 (BaseException) 발생한 경우
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse<?>> handleBaseException(BaseException e) {
        ErrorCode errorCode = e.getErrorCode();
        String errorMessage = e.getMessage();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(BaseResponse.error(errorCode, errorMessage));
    }

    // RequestBody 검증 실패한 경우 (필수 입력 항목 누락 / 형식 오류)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException - " + e.getMessage());

        FieldError fieldError = e.getBindingResult().getFieldError();

        String message = "필수 입력 항목이 누락되었거나 잘못된 형식입니다.";

        // 입력 형식 오류 시
        if (fieldError != null) {
            String code = fieldError.getCode();

            if ("Pattern".equals(code) || "Email".equals(code)) {
                message = fieldError.getDefaultMessage();
            }
        }

        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT_VALUE.getStatus())
                .body(BaseResponse.error(ErrorCode.INVALID_INPUT_VALUE, message));
    }

    // 필수 쿼리 파라미터가 누락된 경우
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<BaseResponse<?>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException - " + e.getMessage());
        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT_VALUE.getStatus())
                .body(BaseResponse.error(ErrorCode.INVALID_INPUT_VALUE, "필수 파라미터가 누락되었습니다."));
    }

    // 쿼리 파라미터 값이 잘못된 경우
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse<?>> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("ConstraintViolationException - " + e.getMessage());
        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT_VALUE.getStatus())
                .body(BaseResponse.error(ErrorCode.INVALID_INPUT_VALUE, "잘못된 쿼리 파라미터 값입니다."));
    }

    // 요청 바디가 비어있거나 JSON 형식이 올바르지 않거나 매핑에 실패한 경우 (ex. 잘못된 Enum값)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<?>> handleEnumParseError(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException - " + e.getMessage());
        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT_VALUE.getStatus())
                .body(BaseResponse.error(ErrorCode.INVALID_INPUT_VALUE, "요청 바디가 비어있거나 JSON 형식이 올바르지 않거나 매핑에 실패했습니다."));
    }

}
