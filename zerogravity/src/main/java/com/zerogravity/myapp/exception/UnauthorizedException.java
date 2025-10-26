package com.zerogravity.myapp.exception;

/**
 * 인증 실패 시 발생하는 예외
 * JWT 토큰이 없거나 유효하지 않은 경우 발생
 * HTTP 401 Unauthorized로 응답됨
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
