package com.zerogravity.myapp.emotion.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import com.zerogravity.myapp.emotion.dto.EmotionRecord;

/**
 * Service interface for emotion record management
 * Handles emotion record retrieval, creation, and updates
 */
public interface EmotionRecordService {

	/**
	 * Get all emotion records for a user
	 * @param userId User ID
	 * @return List of emotion records
	 */
	List<EmotionRecord> getEmotionRecordsByUserId(Long userId);

	/**
	 * Get created time of an emotion record
	 * @param emotionRecordId Emotion record ID
	 * @return Created timestamp
	 */
	Timestamp getCreatedTimeByEmotionRecordId(Long emotionRecordId);

	/**
	 * Get emotion records for a specific period
	 * @param userId User ID
	 * @param periodStart Period start instant (UTC)
	 * @param periodEnd Period end instant (UTC)
	 * @return List of emotion records
	 */
	List<EmotionRecord> getEmotionRecordByPeriodAndUserId(Long userId, Instant periodStart, Instant periodEnd);

	/**
	 * Create a new emotion record with validation
	 * @param userId User ID
	 * @param emotionId Emotion ID (0-6)
	 * @param emotionRecordType Type (daily or moment)
	 * @param emotionReasons List of reasons
	 * @param diaryEntry Diary entry (nullable)
	 * @param timezone User's timezone for daily duplicate check
	 * @param aiAnalysisId AI analysis ID from prediction (nullable)
	 * @param recordDate Optional record date (ISO Date format: YYYY-MM-DD, nullable)
	 * @return Created emotion record ID
	 */
	Long createEmotionRecord(Long userId, Integer emotionId, String emotionRecordType,
	                         List<String> emotionReasons, String diaryEntry, ZoneId timezone, Long aiAnalysisId, String recordDate);

	/**
	 * Update an existing emotion record
	 * @param userId User ID
	 * @param emotionRecordId Emotion record ID
	 * @param emotionId New emotion ID
	 * @param emotionReasons New reasons list
	 * @param diaryEntry New diary entry
	 * @return true if update successful, false otherwise
	 */
	boolean updateEmotionRecord(Long userId, Long emotionRecordId, Integer emotionId,
	                            List<String> emotionReasons, String diaryEntry);
}
