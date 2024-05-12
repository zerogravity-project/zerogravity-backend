package com.zerogravity.myapp.model.service;

import com.zerogravity.myapp.model.dto.PeriodicStatics;

public interface PeriodicStaticsService {
	
	// 감정 기록 통계 조회 
	public abstract PeriodicStatics getPeriodicStaticsByUserId(long userId);
	
	// 감정 기록 통계 삽입
	public boolean upsertPeriodicStatics(PeriodicStatics periodicStatics);


}
