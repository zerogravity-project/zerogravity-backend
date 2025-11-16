package com.zerogravity.myapp.auth.service;

import com.zerogravity.myapp.auth.dao.RefreshTokenDao;
import com.zerogravity.myapp.auth.dto.RefreshToken;
import com.zerogravity.myapp.auth.exception.UnauthorizedException;
import com.zerogravity.myapp.common.security.JWTUtil;
import com.zerogravity.myapp.common.security.SnowflakeIdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Refresh Token Service Implementation
 * Handles secure refresh token operations with rotation and reuse detection
 */
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenServiceImpl.class);

    // Token expiration times
    private static final long ACCESS_TOKEN_EXPIRATION_MS = 15 * 60 * 1000L; // 15 minutes
    private static final int REFRESH_TOKEN_EXPIRATION_DAYS = 30; // 30 days

    // Grace period for concurrent refresh requests (seconds)
    private static final long GRACE_PERIOD_SECONDS = 5L;

    private final RefreshTokenDao refreshTokenDao;
    private final JWTUtil jwtUtil;
    private final SnowflakeIdService snowflakeIdService;

    @Autowired
    public RefreshTokenServiceImpl(
            RefreshTokenDao refreshTokenDao,
            JWTUtil jwtUtil,
            SnowflakeIdService snowflakeIdService
    ) {
        this.refreshTokenDao = refreshTokenDao;
        this.jwtUtil = jwtUtil;
        this.snowflakeIdService = snowflakeIdService;
    }

    /**
     * Create a new refresh token for a user
     * Token is valid for 30 days
     */
    @Override
    @Transactional
    public String createRefreshToken(Long userId) {
        // Generate unique token ID using Snowflake
        Long tokenId = snowflakeIdService.generateId();

        // Generate UUID-based refresh token
        String tokenString = UUID.randomUUID().toString();

        // Calculate expiration (30 days from now)
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(REFRESH_TOKEN_EXPIRATION_DAYS);

        // Create RefreshToken entity
        RefreshToken refreshToken = RefreshToken.builder()
                .tokenId(tokenId)
                .userId(userId)
                .refreshToken(tokenString)
                .expiresAt(expiresAt)
                .createdAt(LocalDateTime.now())
                .isRevoked(false)
                .build();

        // Save to database
        refreshTokenDao.insertRefreshToken(refreshToken);

        logger.info("Created refresh token for user: {}, expires at: {}", userId, expiresAt);

        return tokenString;
    }

    /**
     * Validate refresh token and issue new access token
     * Simplified approach without rotation:
     * - Validates token existence, revocation, and expiration
     * - Issues new access token with same refresh token
     * - Refresh token is reused until 30-day expiration or logout
     */
    @Override
    @Transactional
    public TokenPair validateRefreshToken(String refreshTokenString) {
        // Log token validation attempt (show first 8 chars only for security)
        String tokenPrefix = refreshTokenString != null && refreshTokenString.length() >= 8 ?
                             refreshTokenString.substring(0, 8) + "..." : "null";
        logger.info("Token refresh attempt - token: {}", tokenPrefix);

        // Find token in database
        RefreshToken refreshToken = refreshTokenDao.findByToken(refreshTokenString);

        if (refreshToken == null) {
            logger.warn("REFRESH_TOKEN_INVALID: Token not found in database - token: {}", tokenPrefix);
            throw new UnauthorizedException("REFRESH_TOKEN_INVALID: Token does not exist or has been deleted");
        }

        // Check if token is revoked
        if (Boolean.TRUE.equals(refreshToken.getIsRevoked())) {
            logger.warn("REFRESH_TOKEN_REVOKED: Attempted to use revoked token - user: {}, token: {}",
                    refreshToken.getUserId(), tokenPrefix);
            throw new UnauthorizedException("REFRESH_TOKEN_REVOKED: This token has been revoked. Please login again.");
        }

        // Check if token is expired
        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            logger.warn("REFRESH_TOKEN_EXPIRED: Token expired - user: {}, token: {}, expiresAt: {}",
                    refreshToken.getUserId(), tokenPrefix, refreshToken.getExpiresAt());
            throw new UnauthorizedException("REFRESH_TOKEN_EXPIRED: Token has expired. Please login again.");
        }

        // Generate new access token (reuse same refresh token)
        String newAccessToken = jwtUtil.createJwt(refreshToken.getUserId(), ACCESS_TOKEN_EXPIRATION_MS);

        logger.info("Successfully issued new access token for user: {} - Refresh token: {}",
                refreshToken.getUserId(),
                tokenPrefix);

        return new TokenPair(newAccessToken, refreshTokenString);
    }

    /**
     * Revoke all refresh tokens for a user (used during logout)
     */
    @Override
    @Transactional
    public void revokeAllUserTokens(Long userId) {
        int revokedCount = refreshTokenDao.revokeAllByUserId(userId);
        logger.info("Revoked {} refresh token(s) for user: {}", revokedCount, userId);
    }
}
