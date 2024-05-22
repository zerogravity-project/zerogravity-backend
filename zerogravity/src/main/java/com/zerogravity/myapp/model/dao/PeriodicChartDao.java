package com.zerogravity.myapp.model.dao;

import java.sql.Timestamp;

import org.apache.ibatis.annotations.Param;

import com.zerogravity.myapp.model.dto.PeriodicChart;

public interface PeriodicChartDao {
	
	public abstract PeriodicChart selectPeriodicChart(long userId);
	public abstract PeriodicChart selectPeriodicChartByUserAndType(long userId, String periodType);
	public abstract PeriodicChart selectPeriodicChartByPeriodAndUserId(@Param("userId") long userId, @Param("periodStart") Timestamp periodStart, @Param("periodEnd") Timestamp periodEnd);
	public abstract int insertPeriodicChart(PeriodicChart chart);
	public abstract int updatePeriodicChart(PeriodicChart chart);

}
