package com.zerogravity.myapp.model.service;

import java.sql.Timestamp;

import com.zerogravity.myapp.model.dto.EmotionRecord;
import com.zerogravity.myapp.model.dto.PeriodicChart;

public interface PeriodicChartService {
	
	// 감정 기록 통계 조회 
	public abstract PeriodicChart getPeriodicChartByUserId(long userId);
	
	// 감정 기록 통계 생성 또는 업데이트 
	public abstract boolean createOrModifyPeriodicChart(EmotionRecord emotionRecord, Timestamp createdTime);

}
