package com.zerogravity.myapp.emotion.service;

import com.zerogravity.myapp.emotion.exception.*;
import com.zerogravity.myapp.emotion.dao.EmotionRecordDao;
import com.zerogravity.myapp.emotion.dto.EmotionRecord;
import com.zerogravity.myapp.ai.dao.AIAnalysisCacheDao;
import com.zerogravity.myapp.ai.service.EmotionPredictionService;
import com.zerogravity.myapp.common.security.SnowflakeIdService;
import com.zerogravity.myapp.common.util.TimezoneUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.*;
import java.util.List;

@Service
public class EmotionRecordServiceImpl implements EmotionRecordService {

	private final EmotionRecordDao emotionRecordDao;
	private final SnowflakeIdService snowflakeIdService;
	private final AIAnalysisCacheDao aiAnalysisCacheDao;
	private final EmotionPredictionService emotionPredictionService;

	public EmotionRecordServiceImpl(EmotionRecordDao emotionRecordDao, SnowflakeIdService snowflakeIdService,
								   AIAnalysisCacheDao aiAnalysisCacheDao, EmotionPredictionService emotionPredictionService) {
		this.emotionRecordDao = emotionRecordDao;
		this.snowflakeIdService = snowflakeIdService;
		this.aiAnalysisCacheDao = aiAnalysisCacheDao;
		this.emotionPredictionService = emotionPredictionService;
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmotionRecord> getEmotionRecordsByUserId(Long userId, ZoneId timezone) {
		return emotionRecordDao.selectEmotionRecordByUserId(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public Timestamp getCreatedTimeByEmotionRecordId(Long emotionRecordId, ZoneId timezone) {
		return emotionRecordDao.selectCreatedTimeByEmotionRecordId(emotionRecordId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmotionRecord> getEmotionRecordByPeriodAndUserId(Long userId, Instant periodStart, Instant periodEnd, ZoneId timezone) {
		return emotionRecordDao.selectEmotionRecordByPeriodAndUserId(userId, periodStart, periodEnd);
	}

	@Override
	@Transactional
	public Long createEmotionRecord(Long userId, Integer emotionId, String emotionRecordType,
	                                List<String> emotionReasons, String diaryEntry, ZoneId timezone, Long aiAnalysisId, String recordDate) {

		// Validate emotion record type
		EmotionRecord.Type type;
		try {
			type = EmotionRecord.Type.fromValue(emotionRecordType);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid emotion record type: " + emotionRecordType);
		}

		// Validate emotion ID (0-6)
		if (emotionId < 0 || emotionId > 6) {
			throw new IllegalArgumentException("Invalid emotion ID: " + emotionId + ". Must be between 0 and 6.");
		}

		// Validate emotion reasons
		// Custom reasons are allowed in addition to predefined ones
		if (emotionReasons != null) {
			for (String reason : emotionReasons) {
				if (reason == null || reason.trim().isEmpty()) {
					throw new IllegalArgumentException("Emotion reason cannot be empty");
				}
			}
		}

		// Parse and validate recordDate if provided
		LocalDate targetDate = null;
		Instant recordTimestamp;

		if (recordDate != null && !recordDate.trim().isEmpty()) {
			try {
				targetDate = LocalDate.parse(recordDate); // Parse ISO Date (YYYY-MM-DD)
			} catch (Exception e) {
				throw new IllegalArgumentException("Invalid record date format. Expected YYYY-MM-DD: " + recordDate);
			}

			// Validate: Future dates not allowed
			LocalDate today = LocalDate.now(timezone);

			if (targetDate.isAfter(today)) {
				throw new IllegalArgumentException("Future dates are not allowed. Record date: " + recordDate);
			}

			// Determine timestamp based on date
			if (targetDate.equals(today)) {
				// Today: Use current exact time
				recordTimestamp = Instant.now();
			} else {
				// Past date: Use 12:00:00 (noon) in user timezone, convert to UTC
				ZonedDateTime noonInUserTimezone = targetDate.atTime(12, 0).atZone(timezone);
				recordTimestamp = noonInUserTimezone.toInstant();
			}
		} else {
			// No recordDate provided: Use current time (existing behavior)
			targetDate = LocalDate.now(timezone);
			recordTimestamp = Instant.now();
		}

		// Check for daily record duplicate on target date (using UTC date range for index efficiency)
		if (type == EmotionRecord.Type.DAILY) {
			Instant dateStartUtc = TimezoneUtil.getStartOfDay(targetDate, timezone);
			Instant dateEndUtc = dateStartUtc.plus(Duration.ofDays(1)); // Next day start
			boolean exists = emotionRecordDao.existsDailyRecordForDate(userId, dateStartUtc, dateEndUtc);
			if (exists) {
				throw new DailyRecordAlreadyExistsException();
			}
		}

		// Generate Snowflake ID
		Long emotionRecordId = snowflakeIdService.generateId();

		// Create emotion record
		EmotionRecord record = new EmotionRecord();
		record.setEmotionRecordId(emotionRecordId);
		record.setUserId(userId);
		record.setEmotionId(emotionId);
		record.setEmotionRecordType(type);
		record.setDiaryEntry(diaryEntry);
		record.setCreatedTime(Timestamp.from(recordTimestamp));
		record.setAiAnalysisId(aiAnalysisId);

		int result = emotionRecordDao.createEmotionRecord(record);

		// Insert emotion reasons into junction table
		if (result > 0 && emotionReasons != null && !emotionReasons.isEmpty()) {
			for (String reason : emotionReasons) {
				emotionRecordDao.insertEmotionReason(emotionRecordId, reason);
			}
		}

		// Accept AI prediction if analysis was used
		if (result > 0 && aiAnalysisId != null) {
			emotionPredictionService.acceptPrediction(userId, aiAnalysisId, emotionRecordId);
		}

		// Invalidate AI analysis cache for this user
		if (result > 0) {
			invalidateAICache(userId, recordTimestamp);
		}

		return emotionRecordId;
	}

	@Override
	@Transactional
	public boolean updateEmotionRecord(Long userId, Long emotionRecordId, Integer emotionId,
	                                   List<String> emotionReasons, String diaryEntry, ZoneId timezone) {
		// Fetch existing record
		EmotionRecord existing = emotionRecordDao.selectEmotionRecordByIdAndUserId(emotionRecordId, userId);
		if (existing == null) {
			return false;
		}

		// Check if moment type (moment cannot be edited)
		if (existing.getEmotionRecordType() == EmotionRecord.Type.MOMENT) {
			throw new MomentNotEditableException();
		}

		// Check 24-hour edit window
		Instant createdTime = existing.getCreatedTime().toInstant();
		Duration elapsed = Duration.between(createdTime, Instant.now());
		if (elapsed.toHours() > 24) {
			throw new EditWindowExpiredException();
		}

		// Validate emotion ID
		if (emotionId < 0 || emotionId > 6) {
			throw new IllegalArgumentException("Invalid emotion ID: " + emotionId);
		}

		// Validate emotion reasons
		// Custom reasons are allowed in addition to predefined ones
		if (emotionReasons != null) {
			for (String reason : emotionReasons) {
				if (reason == null || reason.trim().isEmpty()) {
					throw new IllegalArgumentException("Emotion reason cannot be empty");
				}
			}
		}

		// Update emotion record
		EmotionRecord record = new EmotionRecord();
		record.setEmotionRecordId(emotionRecordId);
		record.setUserId(userId);
		record.setEmotionId(emotionId);
		record.setDiaryEntry(diaryEntry);

		boolean updated = emotionRecordDao.updateEmotionRecord(record);

		// Update emotion reasons
		if (updated) {
			emotionRecordDao.deleteEmotionReasons(emotionRecordId);

			if (emotionReasons != null && !emotionReasons.isEmpty()) {
				for (String reason : emotionReasons) {
					emotionRecordDao.insertEmotionReason(emotionRecordId, reason);
				}
			}

			// Invalidate AI analysis cache for this user
			invalidateAICache(userId, createdTime);
		}

		return updated;
	}

	/**
	 * Invalidate AI analysis cache for a user
	 * Deletes cache entries that cover the date range of the emotion record
	 *
	 * @param userId User ID
	 * @param recordTime Timestamp of the emotion record
	 */
	private void invalidateAICache(Long userId, Instant recordTime) {
		ZoneId utcZone = ZoneId.of("UTC");
		LocalDate recordDate = recordTime.atZone(utcZone).toLocalDate();

		// Calculate week range
		LocalDate weekStart = recordDate.minusDays(recordDate.getDayOfWeek().getValue() % 7);
		LocalDate weekEnd = weekStart.plusDays(6);

		// Calculate month range
		LocalDate monthStart = recordDate.withDayOfMonth(1);
		LocalDate monthEnd = recordDate.withDayOfMonth(recordDate.lengthOfMonth());

		// Calculate year range
		LocalDate yearStart = recordDate.withDayOfYear(1);
		LocalDate yearEnd = recordDate.withDayOfYear(recordDate.lengthOfYear());

		// Delete caches for all three periods
		aiAnalysisCacheDao.deleteCacheByUserIdAndDateRange(userId, weekStart, weekEnd);
		aiAnalysisCacheDao.deleteCacheByUserIdAndDateRange(userId, monthStart, monthEnd);
		aiAnalysisCacheDao.deleteCacheByUserIdAndDateRange(userId, yearStart, yearEnd);
	}
}
