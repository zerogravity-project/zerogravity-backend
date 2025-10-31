package com.zerogravity.myapp.emotion.service;

import com.zerogravity.myapp.emotion.exception.*;
import com.zerogravity.myapp.emotion.dao.EmotionRecordDao;
import com.zerogravity.myapp.emotion.dto.EmotionRecord;
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

	public EmotionRecordServiceImpl(EmotionRecordDao emotionRecordDao, SnowflakeIdService snowflakeIdService) {
		this.emotionRecordDao = emotionRecordDao;
		this.snowflakeIdService = snowflakeIdService;
	}

	@Override
	public List<EmotionRecord> getEmotionRecordsByUserId(Long userId) {
		return emotionRecordDao.selectEmotionRecordByUserId(userId);
	}

	@Override
	public Timestamp getCreatedTimeByEmotionRecordId(Long emotionRecordId) {
		return emotionRecordDao.selectCreatedTimeByEmotionRecordId(emotionRecordId);
	}

	@Override
	public List<EmotionRecord> getEmotionRecordByPeriodAndUserId(Long userId, Instant periodStart, Instant periodEnd) {
		return emotionRecordDao.selectEmotionRecordByPeriodAndUserId(userId, periodStart, periodEnd);
	}

	@Override
	@Transactional
	public Long createEmotionRecord(Long userId, Integer emotionId, String emotionRecordType,
	                                List<String> emotionReasons, String diaryEntry, ZoneId timezone) {
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
		if (emotionReasons != null) {
			for (String reason : emotionReasons) {
				if (!EmotionRecord.Reason.isValid(reason)) {
					throw new InvalidReasonException(reason);
				}
			}
		}

		// Check for daily record duplicate
		if (type == EmotionRecord.Type.DAILY) {
			LocalDate today = LocalDate.now(timezone);
			int timezoneOffsetMinutes = timezone.getRules().getOffset(Instant.now()).getTotalSeconds() / 60;
			boolean exists = emotionRecordDao.existsDailyRecordForDate(userId, today, timezoneOffsetMinutes);
			if (exists) {
				throw new DailyRecordAlreadyExistsException();
			}
		}

		// Generate Snowflake ID
		Long emotionRecordId = snowflakeIdService.generateEmotionRecordId();

		// Create emotion record
		EmotionRecord record = new EmotionRecord();
		record.setEmotionRecordId(emotionRecordId);
		record.setUserId(userId);
		record.setEmotionId(emotionId);
		record.setEmotionRecordType(type);
		record.setDiaryEntry(diaryEntry);
		record.setCreatedTime(Timestamp.from(Instant.now()));

		int result = emotionRecordDao.createEmotionRecord(record);

		// Insert emotion reasons into junction table
		if (result > 0 && emotionReasons != null && !emotionReasons.isEmpty()) {
			for (String reason : emotionReasons) {
				emotionRecordDao.insertEmotionReason(emotionRecordId, reason);
			}
		}

		return emotionRecordId;
	}

	@Override
	@Transactional
	public boolean updateEmotionRecord(Long userId, Long emotionRecordId, Integer emotionId,
	                                   List<String> emotionReasons, String diaryEntry) {
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
		if (emotionReasons != null) {
			for (String reason : emotionReasons) {
				if (!EmotionRecord.Reason.isValid(reason)) {
					throw new InvalidReasonException(reason);
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
		}

		return updated;
	}
}
