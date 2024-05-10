package com.zerogravity.myapp.model.service;

import java.sql.Timestamp;
import java.util.Map;

import com.zerogravity.myapp.model.dto.EmotionStatics;

public interface EmotionStaticsService {
	
	public abstract double getEmotionStaticsWeekly(long userId, Timestamp startDate, Timestamp endDate);
	public abstract double getEmotionStaticsMonthly(long userId, Timestamp startDate, Timestamp endDate);
	public abstract double getEmotionStaticsYearly(long userId, Timestamp startDate, Timestamp endDate);
	
	public abstract int createEmotionStaticsWeekly(EmotionStatics emotionStatics);
	public abstract int createEmotionStaticsMonthly(EmotionStatics emotionStatics);
	public abstract int createEmotionStaticsYearly(EmotionStatics emotionStatics);
	
	public abstract boolean modifyEmotionStaticsWeekly(long userId, EmotionStatics emotionStatics);
	public abstract boolean modifyEmotionStaticsMonthly(long userId, EmotionStatics emotionStatics);
	public abstract boolean modifyEmotionStaticsYearly(long userId, EmotionStatics emotionStatics);

}
