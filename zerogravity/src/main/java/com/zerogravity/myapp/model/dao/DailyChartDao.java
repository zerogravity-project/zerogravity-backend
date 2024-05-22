package com.zerogravity.myapp.model.dao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zerogravity.myapp.model.dto.DailyChart;
import com.zerogravity.myapp.model.dto.EmotionRecord;

public interface DailyChartDao {
	
    public abstract DailyChart selectDailyChart(long userId);
    public abstract DailyChart selectDailyChart(String dailyChartId);
    public abstract DailyChart selectDailyChartByDateAndUserId(@Param("createdTime") Timestamp createdTime, @Param("userId") long userId);
    public abstract List<DailyChart> selectDailyChartByPeriodAndUserId(@Param("userId") long userId, @Param("periodStart") Timestamp weekStart, @Param("periodEnd") Timestamp weekEnd);
    public abstract int insertDailyChart(DailyChart dailyChart);
    public abstract int updateDailyChart(DailyChart dailyChart);

}
