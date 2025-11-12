package com.zerogravity.myapp.emotion.dao;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zerogravity.myapp.emotion.dto.EmotionRecord;

/**
 * DAO interface for emotion record database operations
 * Handles emotion records and their associated reasons via junction table
 */
public interface EmotionRecordDao {

	/**
	 * Select all emotion records for a user (excluding soft-deleted)
	 * @param userId User ID
	 * @param timezoneOffset Timezone offset string (e.g., "+09:00")
	 * @return List of emotion records with reasons loaded from junction table
	 */
	List<EmotionRecord> selectEmotionRecordByUserId(@Param("userId") Long userId,
	                                                 @Param("timezoneOffset") String timezoneOffset);

	/**
	 * Select created time of an emotion record
	 * @param emotionRecordId Emotion record ID (Snowflake)
	 * @param timezoneOffset Timezone offset string (e.g., "+09:00")
	 * @return Created timestamp
	 */
	Timestamp selectCreatedTimeByEmotionRecordId(@Param("emotionRecordId") Long emotionRecordId,
	                                             @Param("timezoneOffset") String timezoneOffset);

	/**
	 * Select emotion record by ID for update validation
	 * @param emotionRecordId Emotion record ID
	 * @param userId User ID
	 * @param timezoneOffset Timezone offset string (e.g., "+09:00")
	 * @return Emotion record or null if not found
	 */
	EmotionRecord selectEmotionRecordByIdAndUserId(@Param("emotionRecordId") Long emotionRecordId,
	                                               @Param("userId") Long userId,
	                                               @Param("timezoneOffset") String timezoneOffset);

	/**
	 * Check if a daily emotion record exists for a specific date (uses UTC date range for index efficiency)
	 * @param userId User ID
	 * @param dateStartUtc Start of day in UTC
	 * @param dateEndUtc End of day in UTC (next day start)
	 * @return true if daily record exists for that date
	 */
	boolean existsDailyRecordForDate(@Param("userId") Long userId,
	                                 @Param("dateStartUtc") Instant dateStartUtc,
	                                 @Param("dateEndUtc") Instant dateEndUtc);

	/**
	 * Select emotion records for a specific period and user (excluding soft-deleted)
	 * Returns timestamps in UTC, conversion to user timezone happens in Java layer
	 * @param userId User ID
	 * @param periodStart Period start instant (UTC)
	 * @param periodEnd Period end instant (UTC)
	 * @return List of emotion records within the period
	 */
	List<EmotionRecord> selectEmotionRecordByPeriodAndUserId(@Param("userId") Long userId,
	                                                          @Param("periodStart") Instant periodStart,
	                                                          @Param("periodEnd") Instant periodEnd);

	/**
	 * Select aggregated emotion level statistics for a period
	 * Used for chart /level endpoint
	 * @param userId User ID
	 * @param periodStart Period start instant (UTC)
	 * @param periodEnd Period end instant (UTC)
	 * @param groupBy Grouping strategy (DAY, HOUR, MONTH, etc.)
	 * @return List of aggregated data (label, avgLevel)
	 */
	List<Map<String, Object>> selectEmotionLevelStatsByPeriod(@Param("userId") Long userId,
	                                                           @Param("periodStart") Instant periodStart,
	                                                           @Param("periodEnd") Instant periodEnd,
	                                                           @Param("groupBy") String groupBy);

	/**
	 * Select aggregated emotion reason statistics for a period
	 * Used for chart /reason endpoint
	 * @param userId User ID
	 * @param periodStart Period start instant (UTC)
	 * @param periodEnd Period end instant (UTC)
	 * @return List of aggregated data (reason, count)
	 */
	List<Map<String, Object>> selectEmotionReasonStatsByPeriod(@Param("userId") Long userId,
	                                                            @Param("periodStart") Instant periodStart,
	                                                            @Param("periodEnd") Instant periodEnd);

	/**
	 * Select emotion count statistics for scatter chart
	 * Used for chart /count endpoint
	 * @param userId User ID
	 * @param periodStart Period start instant (UTC)
	 * @param periodEnd Period end instant (UTC)
	 * @param groupBy Grouping strategy (DAY, HOUR, MONTH)
	 * @param timezoneOffset Timezone offset string (e.g., "+09:00")
	 * @return List of aggregated data (timestamp, emotionId, daily_count, moment_count)
	 */
	List<Map<String, Object>> selectEmotionCountStatsByPeriod(@Param("userId") Long userId,
	                                                           @Param("periodStart") Instant periodStart,
	                                                           @Param("periodEnd") Instant periodEnd,
	                                                           @Param("groupBy") String groupBy,
	                                                           @Param("timezoneOffset") String timezoneOffset);

	/**
	 * Create a new emotion record
	 * @param emotionRecord Emotion record to create
	 * @return Number of rows affected
	 */
	int createEmotionRecord(EmotionRecord emotionRecord);

	/**
	 * Update an existing emotion record
	 * @param emotionRecord Emotion record to update
	 * @return true if update successful, false otherwise
	 */
	boolean updateEmotionRecord(EmotionRecord emotionRecord);

	/**
	 * Soft delete an emotion record
	 * @param emotionRecordId Emotion record ID
	 * @param userId User ID
	 * @param deletedAt Deletion timestamp
	 * @return true if delete successful
	 */
	boolean softDeleteEmotionRecord(@Param("emotionRecordId") Long emotionRecordId,
	                                @Param("userId") Long userId,
	                                @Param("deletedAt") Timestamp deletedAt);

	/**
	 * Insert an emotion reason for an emotion record (junction table)
	 * @param emotionRecordId Emotion record ID (Snowflake)
	 * @param emotionReason Emotion reason to insert
	 */
	void insertEmotionReason(@Param("emotionRecordId") Long emotionRecordId, @Param("emotionReason") String emotionReason);

	/**
	 * Delete all emotion reasons for an emotion record (junction table)
	 * @param emotionRecordId Emotion record ID (Snowflake)
	 */
	void deleteEmotionReasons(@Param("emotionRecordId") Long emotionRecordId);

}
