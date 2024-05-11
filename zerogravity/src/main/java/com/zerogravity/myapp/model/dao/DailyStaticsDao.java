package com.zerogravity.myapp.model.dao;

import com.zerogravity.myapp.model.dto.DailyStatics;

public interface DailyStaticsDao {
	
	public abstract DailyStatics selectDailyStatics(long userId);
	public abstract int insertDailyStatics(DailyStatics dailyStatics);
	public abstract int updateDailyStatics(DailyStatics dailyStatics);

}
