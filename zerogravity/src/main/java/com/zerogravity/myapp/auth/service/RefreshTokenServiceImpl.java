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
     * Validate and rotate refresh token
     * Implements security measures:
     * - Checks if token exists and is not expired
     * - Detects token reuse attacks
     * - Revokes old token after use (rotation)
     * - Issues new access token and refresh token
     */
    @Override
    @Transactional
    public TokenPair validateAndRotateRefreshToken(String refreshTokenString) {
        // Find token in database
        RefreshToken refreshToken = refreshTokenDao.findByToken(refreshTokenString);

        if (refreshToken == null) {
            logger.warn("Refresh token not found");
            throw new UnauthorizedException("Invalid refresh token");
        }

        // Check if token is revoked
        if (Boolean.TRUE.equals(refreshToken.getIsRevoked())) {
            logger.warn("Attempting to use revoked refresh token for user: {}", refreshToken.getUserId());
            throw new UnauthorizedException("Refresh token has been revoked");
        }

        // Check if token is expired
        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            logger.warn("Refresh token expired for user: {}", refreshToken.getUserId());
            throw new UnauthorizedException("Refresh token has expired");
        }

        // SECURITY: Detect token reuse attack
        // If token was already used (usedAt is not null), this is a reuse attempt
        if (refreshToken.getUsedAt() != null) {
            // Calculate time since last use
            long secondsSinceUse = Duration.between(refreshToken.getUsedAt(), LocalDateTime.now()).toSeconds();

            // Grace period: Allow concurrent requests within 5 seconds
            if (secondsSinceUse <= GRACE_PERIOD_SECONDS) {
                logger.warn("Concurrent refresh request detected within grace period ({} seconds) for user: {}. Allowing request.",
                        secondsSinceUse, refreshToken.getUserId());
                // Allow the request to proceed - will create new tokens
            } else {
                // Token reuse after grace period indicates potential security threat
                logger.error("SECURITY ALERT: Refresh token reuse detected ({} seconds after use) for user: {}. Revoking all tokens.",
                        secondsSinceUse, refreshToken.getUserId());

                // Revoke all tokens for this user as a security measure
                refreshTokenDao.revokeAllByUserId(refreshToken.getUserId());

                throw new UnauthorizedException("Token reuse detected. All sessions have been terminated for security.");
            }
        }

        // Mark token as used and revoke it (rotation)
        refreshTokenDao.updateUsedAt(refreshToken.getTokenId(), LocalDateTime.now());
        refreshTokenDao.revokeToken(refreshToken.getTokenId());

        // Generate new tokens
        String newAccessToken = jwtUtil.createJwt(refreshToken.getUserId(), ACCESS_TOKEN_EXPIRATION_MS);
        String newRefreshToken = createRefreshToken(refreshToken.getUserId());

        logger.info("Successfully rotated refresh token for user: {}", refreshToken.getUserId());

        return new TokenPair(newAccessToken, newRefreshToken);
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
