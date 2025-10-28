package com.zerogravity.myapp.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "OAuth 인증 응답")
public class AuthResponse {
	@Schema(description = "인증 성공 여부", example = "true")
	private boolean success;
	
	@Schema(description = "응답 메시지", example = "Authentication successful")
	private String message;

	public AuthResponse() {
	}

	public AuthResponse(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "AuthResponse [success=" + success + ", message=" + message + "]";
	}
}