package com.zerogravity.myapp.model.service;

import java.util.List;

import com.zerogravity.myapp.model.dto.EmotionRecord;

public interface EmotionRecordService {

	public abstract List<EmotionRecord> getEmotionRecordsByUserId(long userId);
	
	public abstract int createEmotionRecord(EmotionRecord record);

	public abstract boolean updateEmotionRecord(EmotionRecord record);


}
