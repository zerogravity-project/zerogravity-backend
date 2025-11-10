package com.zerogravity.myapp.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

@Schema(description = "OAuth Authentication Response")
public class AuthResponse {
	@Schema(description = "Authentication success status", example = "true")
	private boolean success;

	@Schema(description = "Response message", example = "Authentication successful")
	private String message;

	@Schema(description = "Flag indicating if user is newly created (needs consent)", example = "true")
	private boolean isNewUser;

	@Schema(description = "User consent information", example = "{\"termsAgreed\": true, \"aiAnalysisConsent\": false}")
	private Map<String, Object> consents;

	@Schema(description = "Backend JWT token for API authentication", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
	private String jwtToken;

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

	public AuthResponse(boolean success, String message, boolean isNewUser, Map<String, Object> consents) {
		this.success = success;
		this.message = message;
		this.isNewUser = isNewUser;
		this.consents = consents;
	}

	public AuthResponse(boolean success, String message, boolean isNewUser, Map<String, Object> consents, String jwtToken) {
		this.success = success;
		this.message = message;
		this.isNewUser = isNewUser;
		this.consents = consents;
		this.jwtToken = jwtToken;
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

	public Map<String, Object> getConsents() {
		return consents;
	}

	public void setConsents(Map<String, Object> consents) {
		this.consents = consents;
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	@Override
	public String toString() {
		return "AuthResponse [success=" + success + ", message=" + message + ", isNewUser=" + isNewUser + ", consents=" + consents + ", jwtToken=" + (jwtToken != null ? "***" : "null") + "]";
	}
}