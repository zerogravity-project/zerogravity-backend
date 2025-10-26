package com.zerogravity.myapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * 전역 예외 처리 핸들러
 * 애플리케이션 전체에서 발생하는 예외를 일관되게 처리
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * UnauthorizedException 처리
     * JWT 토큰이 없거나 유효하지 않은 경우 401 Unauthorized로 응답
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException e) {
        Map<String, Object> response = Map.of(
                "error", "unauthorized",
                "message", e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * 기타 RuntimeException 처리
     * 예기치 않은 에러를 500 Internal Server Error로 응답
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        Map<String, Object> response = Map.of(
                "error", "internal_server_error",
                "message", "요청 처리 중 오류가 발생했습니다."
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
