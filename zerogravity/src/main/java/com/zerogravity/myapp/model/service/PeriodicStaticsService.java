package com.zerogravity.myapp.model.service;

import java.sql.Timestamp;

import com.zerogravity.myapp.model.dto.EmotionRecord;
import com.zerogravity.myapp.model.dto.PeriodicStatics;

public interface PeriodicStaticsService {
	
	// 감정 기록 통계 조회 
	public abstract PeriodicStatics getPeriodicStaticsByUserId(long userId);
	
	// 감정 기록 통계 생성 또는 업데이트 
	public abstract boolean createOrModifyPeriodicStatics(EmotionRecord emotionRecord, Timestamp createdTime);



}
