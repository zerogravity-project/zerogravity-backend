package com.zerogravity.myapp.model.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.zerogravity.myapp.model.dto.DailyChart;
import com.zerogravity.myapp.model.dto.EmotionRecord;
import com.zerogravity.myapp.model.dto.PeriodicChart;

public interface DailyChartService {
	
	
	// 일일 감정 기록 생성 또는 업데이트 
	public abstract boolean createOrModifyDailyChart(EmotionRecord emotionRecord, Timestamp createdTime);
	
	public abstract List<DailyChart> getMonthlyChartByPeriodAndUserId(long userId, Timestamp searchDateTime);

	public abstract List<DailyChart> getWeeklyChartByPeriodAndUserId(long userId, Timestamp searchDateTime);

	public abstract List<DailyChart> getYearlyChartByPeriodAndUserId(long userId, Timestamp searchDateTime);



}
