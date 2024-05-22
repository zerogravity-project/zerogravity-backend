package com.zerogravity.myapp.model.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.zerogravity.myapp.model.dto.DailyChart;
import com.zerogravity.myapp.model.dto.EmotionRecord;

public interface EmotionRecordService {

	public abstract List<EmotionRecord> getEmotionRecordsByUserId(long userId);
	
	public abstract Timestamp getCreatedTimeByEmotionRecordId(String emotionRecordId);
	
	public abstract List<EmotionRecord> getEmotionRecordByPeriodAndUserId(long userId, Timestamp searchDateTime);

	public abstract int createEmotionRecord(EmotionRecord record);

	public abstract boolean updateEmotionRecord(EmotionRecord record);

}
