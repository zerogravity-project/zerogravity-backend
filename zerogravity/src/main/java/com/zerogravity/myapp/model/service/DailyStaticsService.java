package com.zerogravity.myapp.model.service;

import com.zerogravity.myapp.model.dto.EmotionRecord;

public interface DailyStaticsService {
	
	// 일일 감정 기록 조회	
	public abstract boolean updateOrCreateDailyStatics(EmotionRecord emotionRecord);

}
