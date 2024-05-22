package com.zerogravity.myapp.model.dao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zerogravity.myapp.model.dto.DailyChart;
import com.zerogravity.myapp.model.dto.EmotionRecord;

public interface EmotionRecordDao {
	
	public abstract List<EmotionRecord> selectEmotionRecordByUserId(long userId);
	public abstract Timestamp selectCreatedTimeByEmotionRecordId(String emotionRecordId);
//	public abstract List<EmotionRecord> selectEmotionRecordByPeriodAndUserId(@Param("userId") long userId, @Param("periodStart") Timestamp weekStart, @Param("periodEnd") Timestamp weekEnd);
	public abstract int createEmotionRecord(EmotionRecord emotionRecord);
	public abstract boolean updateEmotionRecord(EmotionRecord emotionRecord);
	public abstract List<EmotionRecord> selectEmotionRecordByPeriodAndUserId(long userId, Timestamp periodStart, Timestamp periodEnd);

}
