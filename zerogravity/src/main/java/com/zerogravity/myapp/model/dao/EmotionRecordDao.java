package com.zerogravity.myapp.model.dao;

import java.util.List;

import com.zerogravity.myapp.model.dto.EmotionRecord;

public interface EmotionRecordDao {
	
	public abstract List<EmotionRecord> selectEmotionRecordByEmotionId(long userId);
	public abstract int createEmotionRecord(EmotionRecord record);
	public abstract boolean updateEmotionRecord(EmotionRecord record);

}
