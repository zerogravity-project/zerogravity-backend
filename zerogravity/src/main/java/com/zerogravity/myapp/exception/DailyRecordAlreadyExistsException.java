package com.zerogravity.myapp.exception;

/**
 * Exception thrown when attempting to create a second daily emotion record on the same day
 *
 * Business Rule: Users can only create ONE daily record per day (in their timezone)
 */
public class DailyRecordAlreadyExistsException extends RuntimeException {
	public DailyRecordAlreadyExistsException() {
		super("Daily emotion record already exists for today");
	}

	public DailyRecordAlreadyExistsException(String message) {
		super(message);
	}
}
