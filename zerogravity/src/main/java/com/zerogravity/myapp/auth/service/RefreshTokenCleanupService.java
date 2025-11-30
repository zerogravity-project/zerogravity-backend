package com.zerogravity.myapp.auth.service;

import com.zerogravity.myapp.auth.dao.RefreshTokenDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Refresh Token Cleanup Service
 * Scheduled task to clean up expired and revoked refresh tokens
 */
@Service
public class RefreshTokenCleanupService {

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenCleanupService.class);

    // Delete revoked tokens older than 1 day
    private static final int REVOKED_TOKEN_RETENTION_HOURS = 24;

    private final RefreshTokenDao refreshTokenDao;

    @Autowired
    public RefreshTokenCleanupService(RefreshTokenDao refreshTokenDao) {
        this.refreshTokenDao = refreshTokenDao;
    }

    /**
     * Clean up expired and old revoked refresh tokens
     * Runs hourly at the start of every hour
     * Deletes:
     * 1. Expired tokens that are already revoked
     * 2. Revoked tokens older than 24 hours
     */
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void cleanupOldTokens() {
        logger.info("Starting refresh token cleanup job");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cutoffTime = now.minusHours(REVOKED_TOKEN_RETENTION_HOURS);

        try {
            // Delete expired tokens (already revoked)
            int expiredDeleted = refreshTokenDao.deleteExpiredTokens(now);
            logger.info("Deleted {} expired refresh tokens", expiredDeleted);

            // Delete old revoked tokens (older than 24 hours)
            int oldRevokedDeleted = refreshTokenDao.deleteOldRevokedTokens(cutoffTime);
            logger.info("Deleted {} old revoked refresh tokens (older than {} hours)",
                    oldRevokedDeleted, REVOKED_TOKEN_RETENTION_HOURS);

            logger.info("Refresh token cleanup completed - Total deleted: {}",
                    expiredDeleted + oldRevokedDeleted);

        } catch (Exception e) {
            logger.error("Error during refresh token cleanup", e);
        }
    }
}
