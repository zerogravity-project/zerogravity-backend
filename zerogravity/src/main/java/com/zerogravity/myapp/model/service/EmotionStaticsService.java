package com.zerogravity.myapp.model.service;

public interface EmotionStaticsService {
	
	public abstract double getEmotionStaticsWeekly(long userId);
	public abstract double getEmotionStaticsMonthly(long userId);
	public abstract double getEmotionStaticsYearly(long userId);

}
