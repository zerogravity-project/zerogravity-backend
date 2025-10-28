package com.zerogravity.myapp.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "에러 응답")
public class ErrorResponse {

	@Schema(description = "에러 코드", example = "DAILY_ALREADY_EXISTS")
	private String error;

	@Schema(description = "에러 메시지", example = "오늘 이미 daily 기록이 있습니다.")
	private String message;

	@Schema(description = "타임스탬프 (ISO 8601 with offset)", example = "2025-10-26T15:00:00+09:00")
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
