package com.zerogravity.myapp.emotion.exception;

/**
 * Exception thrown when attempting to edit an emotion record outside the 24-hour window
 *
 * Business Rule: Emotion records can only be edited within 24 hours of creation
 */
public class EditWindowExpiredException extends RuntimeException {
	public EditWindowExpiredException() {
		super("Edit window expired. Records can only be edited within 24 hours of creation");
	}

	public EditWindowExpiredException(String message) {
		super(message);
	}
}
