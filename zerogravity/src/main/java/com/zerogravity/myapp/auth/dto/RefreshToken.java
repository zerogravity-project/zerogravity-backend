package com.zerogravity.myapp.auth.dto;

import java.time.LocalDateTime;

/**
 * Refresh Token DTO
 * Represents a refresh token used for JWT token renewal
 * Supports token rotation and reuse detection
 */
public class RefreshToken {

    /**
     * Unique token ID (Snowflake ID)
     */
    private Long tokenId;

    /**
     * User ID associated with this token
     */
    private Long userId;

    /**
     * UUID-based refresh token string
     */
    private String refreshToken;

    /**
     * Token expiration timestamp (30 days from creation)
     */
    private LocalDateTime expiresAt;

    /**
     * Token creation timestamp
     */
    private LocalDateTime createdAt;

    /**
     * Last used timestamp (null if never used)
     * Used for detecting token reuse attacks
     */
    private LocalDateTime usedAt;

    /**
     * Revocation flag
     * Set to true when user logs out or token is invalidated
     */
    private Boolean isRevoked;

    // Constructors
    public RefreshToken() {
    }

    public RefreshToken(Long tokenId, Long userId, String refreshToken, LocalDateTime expiresAt,
                        LocalDateTime createdAt, LocalDateTime usedAt, Boolean isRevoked) {
        this.tokenId = tokenId;
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
        this.usedAt = usedAt;
        this.isRevoked = isRevoked;
    }

    // Builder pattern
    public static RefreshTokenBuilder builder() {
        return new RefreshTokenBuilder();
    }

    public static class RefreshTokenBuilder {
        private Long tokenId;
        private Long userId;
        private String refreshToken;
        private LocalDateTime expiresAt;
        private LocalDateTime createdAt;
        private LocalDateTime usedAt;
        private Boolean isRevoked;

        public RefreshTokenBuilder tokenId(Long tokenId) {
            this.tokenId = tokenId;
            return this;
        }

        public RefreshTokenBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public RefreshTokenBuilder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public RefreshTokenBuilder expiresAt(LocalDateTime expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public RefreshTokenBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public RefreshTokenBuilder usedAt(LocalDateTime usedAt) {
            this.usedAt = usedAt;
            return this;
        }

        public RefreshTokenBuilder isRevoked(Boolean isRevoked) {
            this.isRevoked = isRevoked;
            return this;
        }

        public RefreshToken build() {
            return new RefreshToken(tokenId, userId, refreshToken, expiresAt, createdAt, usedAt, isRevoked);
        }
    }

    // Getters and Setters
    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(LocalDateTime usedAt) {
        this.usedAt = usedAt;
    }

    public Boolean getIsRevoked() {
        return isRevoked;
    }

    public void setIsRevoked(Boolean isRevoked) {
        this.isRevoked = isRevoked;
    }
}
