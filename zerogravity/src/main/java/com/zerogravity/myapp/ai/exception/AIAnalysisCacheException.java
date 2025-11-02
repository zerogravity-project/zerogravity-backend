package com.zerogravity.myapp.ai.exception;

/**
 * Exception thrown when AI analysis cache operations fail
 */
public class AIAnalysisCacheException extends RuntimeException {

	public AIAnalysisCacheException(String message) {
		super(message);
	}

	public AIAnalysisCacheException(String message, Throwable cause) {
		super(message, cause);
	}

	public AIAnalysisCacheException(Throwable cause) {
		super(cause);
	}
}
