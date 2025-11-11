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

    private final RefreshTokenDao refreshTokenDao;

    @Autowired
    public RefreshTokenCleanupService(RefreshTokenDao refreshTokenDao) {
        this.refreshTokenDao = refreshTokenDao;
    }

    /**
     * Clean up expired refresh tokens
     * Runs daily at 2:00 AM server time
     * Deletes tokens that are both expired AND revoked
     */
    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void cleanupExpiredTokens() {
        try {
            LocalDateTime now = LocalDateTime.now();
            int deletedCount = refreshTokenDao.deleteExpiredTokens(now);
            logger.info("Refresh token cleanup completed. Deleted {} expired and revoked tokens.", deletedCount);
        } catch (Exception e) {
            logger.error("Error during refresh token cleanup", e);
        }
    }
}
