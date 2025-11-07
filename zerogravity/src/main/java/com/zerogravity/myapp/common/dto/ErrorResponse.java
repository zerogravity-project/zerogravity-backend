package com.zerogravity.myapp.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Error Response")
public class ErrorResponse {

	@Schema(description = "Error code", example = "DAILY_ALREADY_EXISTS")
	private String error;

	@Schema(description = "Error message", example = "Daily record already exists for today.")
	private String message;

	@Schema(description = "Timestamp (ISO 8601 with offset)", example = "2025-10-26T15:00:00+09:00")
	private String timestamp;

	public ErrorResponse() {}

	public ErrorResponse(String error, String message, String timestamp) {
		this.error = error;
		this.message = message;
		this.timestamp = timestamp;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
