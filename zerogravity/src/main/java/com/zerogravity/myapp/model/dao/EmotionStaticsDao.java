package com.zerogravity.myapp.model.dao;

import java.util.Map;

import com.zerogravity.myapp.model.dto.EmotionStatics;

public interface EmotionStaticsDao {
	
	public abstract double selectAvgWeekly(Map<String, Object> information);
	public abstract double selectAvgMonthly(Map<String, Object> userIdAndDate);
	public abstract double selectAvgYearly(Map<String, Object> userIdAndDate);
	
	public abstract int insertAvgWeekly(EmotionStatics emotionStataics);
	public abstract int insertAvgMonthly(EmotionStatics emotionStataics);
	public abstract int insertAvgYearly(EmotionStatics emotionStataics);
	
	public abstract boolean updateAvgWeekly(long userId, EmotionStatics emotionStatics);
	public abstract boolean updateAvgMontyly(long userId, EmotionStatics emotionStatics);
	public abstract boolean updateAvgYearly(long userId, EmotionStatics emotionStatics);
	
}
