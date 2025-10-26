package com.zerogravity.myapp.model.dao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zerogravity.myapp.model.dto.EmotionRecord;

/**
 * DAO interface for emotion record database operations
 * Handles emotion records and their associated reasons via junction table
 */
public interface EmotionRecordDao {

	/**
	 * Select all emotion records for a user
	 * @param userId User ID
	 * @return List of emotion records with reasons loaded from junction table
	 */
	public abstract List<EmotionRecord> selectEmotionRecordByUserId(long userId);

	/**
	 * Select created time of an emotion record
	 * @param emotionRecordId Emotion record ID
	 * @return Created timestamp
	 */
	public abstract Timestamp selectCreatedTimeByEmotionRecordId(String emotionRecordId);

	/**
	 * Select emotion records for a specific period and user
	 * @param userId User ID
	 * @param periodStart Period start timestamp
	 * @param periodEnd Period end timestamp
	 * @return List of emotion records within the period
	 */
	public abstract List<EmotionRecord> selectEmotionRecordByPeriodAndUserId(@Param("userId") long userId, @Param("periodStart") Timestamp periodStart, @Param("periodEnd") Timestamp periodEnd);

	/**
	 * Select emotion records for chart statistics
	 * @param userId User ID
	 * @param periodStart Period start timestamp
	 * @param periodEnd Period end timestamp
	 * @return List of emotion records for charting
	 */
	public abstract List<EmotionRecord> selectEmotionRecordByPeriodAndUserIdForChart(@Param("userId") long userId, @Param("periodStart") Timestamp periodStart, @Param("periodEnd") Timestamp periodEnd);

	/**
	 * Create a new emotion record
	 * @param emotionRecord Emotion record to create
	 * @return Number of rows affected
	 */
	public abstract int createEmotionRecord(EmotionRecord emotionRecord);

	/**
	 * Update an existing emotion record
	 * @param emotionRecord Emotion record to update
	 * @return true if update successful, false otherwise
	 */
	public abstract boolean updateEmotionRecord(EmotionRecord emotionRecord);

	/**
	 * Insert an emotion reason for an emotion record (junction table)
	 * @param emotionRecordId Emotion record ID
	 * @param emotionReason Emotion reason to insert
	 */
	public abstract void insertEmotionReason(@Param("emotionRecordId") String emotionRecordId, @Param("emotionReason") String emotionReason);

	/**
	 * Delete all emotion reasons for an emotion record (junction table)
	 * @param emotionRecordId Emotion record ID
	 */
	public abstract void deleteEmotionReasons(String emotionRecordId);

}
