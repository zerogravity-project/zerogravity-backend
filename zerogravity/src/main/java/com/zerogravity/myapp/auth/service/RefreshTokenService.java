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
     * Validate refresh token and issue new access token
     * Simplified approach without token rotation:
     * - Validates token (expiration, revocation)
     * - Returns new access token with same refresh token
     * - Refresh token is reused for 30 days until expiration
     *
     * @param refreshToken refresh token string
     * @return TokenPair containing new access token and same refresh token
     * @throws com.zerogravity.myapp.auth.exception.UnauthorizedException if token is invalid, expired, or revoked
     */
    TokenPair validateRefreshToken(String refreshToken);

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
