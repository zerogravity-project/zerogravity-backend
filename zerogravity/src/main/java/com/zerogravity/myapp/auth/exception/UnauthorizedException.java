package com.zerogravity.myapp.auth.exception;

/**
 * Exception thrown when authentication fails
 * Occurs when JWT token is missing or invalid
 * Returns HTTP 401 Unauthorized response
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
