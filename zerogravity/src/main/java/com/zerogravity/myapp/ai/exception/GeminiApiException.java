package com.zerogravity.myapp.ai.exception;

/**
 * Exception thrown when Gemini API call fails
 */
public class GeminiApiException extends RuntimeException {

	public GeminiApiException(String message) {
		super(message);
	}

	public GeminiApiException(String message, Throwable cause) {
		super(message, cause);
	}

	public GeminiApiException(Throwable cause) {
		super(cause);
	}
}
