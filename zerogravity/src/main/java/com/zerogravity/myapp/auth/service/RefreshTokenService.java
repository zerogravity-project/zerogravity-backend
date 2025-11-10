package com.zerogravity.myapp.auth.service;

/**
 * Refresh Token Service Interface
 * Handles refresh token creation, validation, rotation, and revocation
 */
public interface RefreshTokenService {

    /**
     * Create a new refresh token for a user
     * @param userId user ID
     * @return generated refresh token string (UUID)
     */
    String createRefreshToken(Long userId);

    /**
     * Validate and rotate refresh token (issue new tokens)
     * Implements token rotation security pattern:
     * - Validates token (expiration, revocation)
     * - Detects token reuse attacks
     * - Revokes old token
     * - Creates new refresh token
     * - Returns new access token and refresh token
     *
     * @param refreshToken refresh token string
     * @return TokenPair containing new access token and refresh token
     * @throws com.zerogravity.myapp.auth.exception.UnauthorizedException if token is invalid or reused
     */
    TokenPair validateAndRotateRefreshToken(String refreshToken);

    /**
     * Revoke all refresh tokens for a user (logout)
     * @param userId user ID
     */
    void revokeAllUserTokens(Long userId);

    /**
     * Token pair response containing access token and refresh token
     */
    record TokenPair(String accessToken, String refreshToken) {}
}
