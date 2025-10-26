package com.zerogravity.myapp.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * OAuth authentication response
 * Contains backend JWT token for NextAuth integration
 */
@Schema(description = "OAuth 인증 응답")
public class AuthResponse {
	@Schema(description = "백엔드 JWT 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
	private String token;

	public AuthResponse() {
	}

	public AuthResponse(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "AuthResponse [token=" + token + "]";
	}
}
