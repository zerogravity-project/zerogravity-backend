package com.zerogravity.myapp.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "OAuth Authentication Response")
public class AuthResponse {
	@Schema(description = "Authentication success status", example = "true")
	private boolean success;
	
	@Schema(description = "Response message", example = "Authentication successful")
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