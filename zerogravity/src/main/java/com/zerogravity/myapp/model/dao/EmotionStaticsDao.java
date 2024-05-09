package com.zerogravity.myapp.model.dao;

import java.math.BigInteger;

public interface EmotionStaticsDao {
	
	public abstract double selectAvgWeekly(long userId);
	public abstract double selectAvgMonthly(long userId);
	public abstract double selectAvgYearly(long userId);
	
}
