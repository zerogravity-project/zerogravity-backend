package com.zerogravity.myapp.auth.exception;

/**
 * Exception thrown when a deactivated (soft-deleted) user attempts to login
 * Returns HTTP 409 Conflict response
 */
public class UserDeactivatedException extends RuntimeException {

    public UserDeactivatedException(String message) {
        super(message);
    }
}
