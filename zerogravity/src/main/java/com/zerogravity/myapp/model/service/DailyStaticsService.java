package com.zerogravity.myapp.model.service;

import com.zerogravity.myapp.model.dto.DailyStatics;

public interface DailyStaticsService {
	
	// 일일 감정 기록 조회
	public abstract DailyStatics getDailyStaticsByUserId(long userId);
	
	// 일일 감정 기록 삽입
	public abstract boolean createDailyStatics(DailyStatics dailyStatics);
	
	// 일일 감정 기록 업데이트 
	public abstract boolean modifyDailyStatics(DailyStatics dailyStatics);

}
