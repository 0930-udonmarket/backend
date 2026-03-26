package com.udonmarket.backend.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* ================================================
       CustomException - ErrorCode 기반 예외 처리
       예) throw new CustomException(ErrorCode.USER_NOT_FOUND)
    ================================================ */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, String>> handleCustomException(CustomException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(Map.of("message", e.getMessage()));
    }

    /* ================================================
       IllegalArgumentException - 잘못된 인자값
       예) throw new IllegalArgumentException("이미 사용 중인 이메일입니다")
    ================================================ */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT_VALUE.getStatus())
                .body(Map.of("message", e.getMessage()));
    }

    /* ================================================
       IllegalStateException - 잘못된 상태
       예) throw new IllegalStateException("이미 마감된 공구입니다")
    ================================================ */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalState(IllegalStateException e) {
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(Map.of("message", e.getMessage()));
    }

    /* ================================================
       Exception - 그 외 모든 예외 처리
    ================================================ */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(Map.of("message", ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
    }
}