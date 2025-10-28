package com.zerogravity.myapp.exception;

/**
 * Exception thrown when an invalid emotion reason is provided
 *
 * Business Rule: Only predefined emotion reasons are allowed
 */
public class InvalidReasonException extends RuntimeException {
	public InvalidReasonException(String reason) {
		super("Invalid emotion reason: " + reason);
	}

	public InvalidReasonException(String reason, Throwable cause) {
		super("Invalid emotion reason: " + reason, cause);
	}
}
