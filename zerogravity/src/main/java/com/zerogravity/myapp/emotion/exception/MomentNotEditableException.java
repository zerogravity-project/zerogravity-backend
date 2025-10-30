package com.zerogravity.myapp.emotion.exception;

/**
 * Exception thrown when attempting to edit a moment-type emotion record
 *
 * Business Rule: Only daily-type records can be edited; moment-type records are immutable
 */
public class MomentNotEditableException extends RuntimeException {
	public MomentNotEditableException() {
		super("Moment-type emotion records cannot be edited");
	}

	public MomentNotEditableException(String message) {
		super(message);
	}
}
