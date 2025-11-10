package com.zerogravity.myapp.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response DTO for token refresh endpoint
 */
@Schema(description = "Refresh token response with new access and refresh tokens")
public class RefreshResponse {

    @Schema(description = "New access token (JWT)", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "New refresh token (rotated)", example = "660e8400-e29b-41d4-a716-446655440001")
    private String refreshToken;

    public RefreshResponse() {
    }

    public RefreshResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
