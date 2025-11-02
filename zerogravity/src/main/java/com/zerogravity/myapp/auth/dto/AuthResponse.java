package com.zerogravity.myapp.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "OAuth Authentication Response")
public class AuthResponse {
	@Schema(description = "Authentication success status", example = "true")
	private boolean success;

	@Schema(description = "Response message", example = "Authentication successful")
	private String message;

	@Schema(description = "Flag indicating if user is newly created (needs consent)", example = "true")
	private boolean isNewUser;

	public AuthResponse() {
	}

	public AuthResponse(boolean success, String message) {
		this.success = success;
		this.message = message;
		this.isNewUser = false;
	}

	public AuthResponse(boolean success, String message, boolean isNewUser) {
		this.success = success;
		this.message = message;
		this.isNewUser = isNewUser;
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

	public boolean isNewUser() {
		return isNewUser;
	}

	public void setNewUser(boolean newUser) {
		isNewUser = newUser;
	}

	@Override
	public String toString() {
		return "AuthResponse [success=" + success + ", message=" + message + ", isNewUser=" + isNewUser + "]";
	}
}