package com.zerogravity.myapp.auth.dao;

import com.zerogravity.myapp.auth.dto.RefreshToken;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

/**
 * MyBatis Mapper for refresh_tokens table operations
 * Handles CRUD operations for refresh tokens
 */
@Mapper
public interface RefreshTokenDao {

    /**
     * Insert a new refresh token
     * @param refreshToken refresh token to insert
     * @return number of rows affected
     */
    int insertRefreshToken(RefreshToken refreshToken);

    /**
     * Find refresh token by token string
     * @param token refresh token string
     * @return RefreshToken if found, null otherwise
     */
    RefreshToken findByToken(String token);

    /**
     * Update used_at timestamp when token is used
     * @param tokenId token ID to update
     * @param usedAt timestamp when token was used
     * @return number of rows affected
     */
    int updateUsedAt(Long tokenId, LocalDateTime usedAt);

    /**
     * Revoke a specific token
     * @param tokenId token ID to revoke
     * @return number of rows affected
     */
    int revokeToken(Long tokenId);

    /**
     * Revoke all tokens for a specific user (for logout)
     * @param userId user ID
     * @return number of rows affected
     */
    int revokeAllByUserId(Long userId);

    /**
     * Delete expired tokens (for cleanup batch job)
     * @param now current timestamp
     * @return number of rows deleted
     */
    int deleteExpiredTokens(LocalDateTime now);

    /**
     * Delete old revoked tokens (cleanup batch job)
     * Deletes revoked tokens older than the specified cutoff time
     * @param cutoffTime tokens revoked before this time will be deleted
     * @return number of rows deleted
     */
    int deleteOldRevokedTokens(LocalDateTime cutoffTime);
}
