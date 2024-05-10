package com.zerogravity.myapp.model.dao;

import com.zerogravity.myapp.model.dto.EmotionStatics;

public interface EmotionStaticsDao {
	
	public abstract double selectAvgWeekly(long userId);
	public abstract double selectAvgMonthly(long userId);
	public abstract double selectAvgYearly(long userId);
	
	public abstract int insertAvgWeekly(EmotionStatics emotionStataics);
	public abstract int insertAvgMonthly(EmotionStatics emotionStataics);
	public abstract int insertAvgYearly(EmotionStatics emotionStataics);
	
	public abstract boolean updateAvgWeekly(long userId, EmotionStatics emotionStatics);
	public abstract boolean updateAvgMontyly(long userId, EmotionStatics emotionStatics);
	public abstract boolean updateAvgYearly(long userId, EmotionStatics emotionStatics);
	
}
