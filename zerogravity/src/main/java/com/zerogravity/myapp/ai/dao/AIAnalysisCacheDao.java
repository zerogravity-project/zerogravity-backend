package com.zerogravity.myapp.ai.dao;

import com.zerogravity.myapp.ai.dto.AIAnalysisCache;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;

/**
 * DAO interface for AI analysis cache database operations
 * Handles cache retrieval, insertion, and invalidation
 */
@Mapper
public interface AIAnalysisCacheDao {

	/**
	 * Select valid cache entry for a user, period, and start date
	 * Valid means: expires_at > NOW()
	 *
	 * @param userId User ID
	 * @param period Period type (WEEK, MONTH, YEAR)
	 * @param startDate Start date of the period
	 * @return Cache entry if valid and exists, null otherwise
	 */
	AIAnalysisCache selectValidCache(@Param("userId") Long userId,
									 @Param("period") String period,
									 @Param("startDate") LocalDate startDate);

	/**
	 * Insert a new cache entry
	 *
	 * @param cache Cache entry to insert
	 * @return Number of rows affected
	 */
	int insertCache(AIAnalysisCache cache);

	/**
	 * Delete cache entries for a user within a date range
	 * Used for cache invalidation when emotion records are created/updated
	 *
	 * @param userId User ID
	 * @param startDate Start date (inclusive)
	 * @param endDate End date (inclusive)
	 * @return Number of rows affected
	 */
	int deleteCacheByUserIdAndDateRange(@Param("userId") Long userId,
									   @Param("startDate") LocalDate startDate,
									   @Param("endDate") LocalDate endDate);

	/**
	 * Delete all expired cache entries
	 * Useful for periodic cleanup
	 *
	 * @return Number of rows affected
	 */
	int deleteExpiredCache();
}
