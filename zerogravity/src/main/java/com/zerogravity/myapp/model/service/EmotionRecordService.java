package com.zerogravity.myapp.model.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.zerogravity.myapp.model.dto.DailyChart;
import com.zerogravity.myapp.model.dto.EmotionRecord;

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
	public abstract List<EmotionRecord> getEmotionRecordsByUserId(long userId);

	/**
	 * Get created time of an emotion record
	 * @param emotionRecordId Emotion record ID
	 * @return Created timestamp
	 */
	public abstract Timestamp getCreatedTimeByEmotionRecordId(String emotionRecordId);

	/**
	 * Get emotion records for a period
	 * @param userId User ID
	 * @param searchDateTime Search date time
	 * @return List of emotion records
	 */
	public abstract List<EmotionRecord> getEmotionRecordByPeriodAndUserId(long userId, Timestamp searchDateTime);

	/**
	 * Get emotion records for monthly period
	 * @param userId User ID
	 * @param searchDateTime Search date time
	 * @return List of emotion records
	 */
	public abstract List<EmotionRecord> getMonthlyEmotionRecordByPeriodAndUserId(long userId, Timestamp searchDateTime);

	/**
	 * Get emotion records for weekly period
	 * @param userId User ID
	 * @param searchDateTime Search date time
	 * @return List of emotion records
	 */
	public abstract List<EmotionRecord> getWeeklyEmotionRecordByPeriodAndUserId(long userId, Timestamp searchDateTime);

	/**
	 * Get emotion records for yearly period
	 * @param userId User ID
	 * @param searchDateTime Search date time
	 * @return List of emotion records
	 */
	public abstract List<EmotionRecord> getYearlyEmotionRecordByPeriodAndUserId(long userId, Timestamp searchDateTime);

	/**
	 * Create a new emotion record
	 * @param record Emotion record to create
	 * @return Number of rows affected
	 */
	public abstract int createEmotionRecord(EmotionRecord record);

	/**
	 * Update an existing emotion record
	 * @param record Emotion record to update
	 * @return true if update successful, false otherwise
	 */
	public abstract boolean updateEmotionRecord(EmotionRecord record);

	/**
	 * Get emotion records for a specific year and month
	 * @param userId User ID
	 * @param year Year
	 * @param month Month
	 * @return List of emotion records
	 */
	public abstract List<EmotionRecord> getEmotionRecordByYearAndMonth(long userId, int year, int month);

	/**
	 * Get emotion records for a specific year, month, and week
	 * @param userId User ID
	 * @param year Year
	 * @param month Month
	 * @param week Week (ISO week number)
	 * @return List of emotion records
	 */
	public abstract List<EmotionRecord> getEmotionRecordByYearMonthWeek(long userId, int year, int month, int week);

	/**
	 * Get emotion records for a specific date (detailed view)
	 * @param userId User ID
	 * @param date Local date
	 * @return List of emotion records for the date
	 */
	public abstract List<EmotionRecord> getEmotionRecordByDate(long userId, LocalDate date);

}
