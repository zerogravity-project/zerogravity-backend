package com.zerogravity.myapp.model.service;

import java.sql.Timestamp;

import com.zerogravity.myapp.model.dto.EmotionRecord;

public interface DailyStaticsService {
	
	// 일일 감정 기록 생성 또는 업데이트 
	public abstract boolean createOrModifyDailyStatics(EmotionRecord emotionRecord, Timestamp createdTime);


}
