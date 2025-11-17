package com.zerogravity.myapp.ai.service;

import com.zerogravity.myapp.ai.dao.AIAnalysisCacheDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Scheduled service for cleaning up expired AI analysis cache entries
 * Runs daily to remove cache entries that have passed their TTL (24 hours)
 */
@Service
public class AIAnalysisCacheCleanupService {

	private static final Logger logger = LoggerFactory.getLogger(AIAnalysisCacheCleanupService.class);

	private final AIAnalysisCacheDao aiAnalysisCacheDao;

	public AIAnalysisCacheCleanupService(AIAnalysisCacheDao aiAnalysisCacheDao) {
		this.aiAnalysisCacheDao = aiAnalysisCacheDao;
	}

	/**
	 * Clean up expired cache entries
	 * Runs daily at 3:00 AM server time
	 * Deletes cache entries where expires_at < NOW()
	 */
	@Scheduled(cron = "0 0 3 * * *")
	public void cleanupExpiredCache() {
		try {
			logger.info("Starting AI analysis cache cleanup...");
			int deletedCount = aiAnalysisCacheDao.deleteExpiredCache();
			logger.info("AI analysis cache cleanup completed. Deleted {} expired cache entries", deletedCount);
		} catch (Exception e) {
			logger.error("Error during AI analysis cache cleanup: {}", e.getMessage(), e);
		}
	}
}
