package com.zerogravity.myapp.model.dao;

import java.sql.Timestamp;
import java.util.List;

import com.zerogravity.myapp.model.dto.EmotionRecord;

public interface EmotionRecordDao {
	
	public abstract List<EmotionRecord> selectEmotionRecordByUserId(long userId);
	public abstract Timestamp selectCreatedTimeByEmotionRecordId(String emotionRecordId);
	public abstract int createEmotionRecord(EmotionRecord record);
	public abstract boolean updateEmotionRecord(EmotionRecord record);

}
