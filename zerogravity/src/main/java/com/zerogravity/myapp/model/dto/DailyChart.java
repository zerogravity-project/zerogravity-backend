package com.zerogravity.myapp.model.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DailyChart {
	
	private String dailyChartId;
	private long userId;
	private int dailySum;
	private int dailyCount;
	private double dailyAverage;
	private Timestamp createdTime;
	private Timestamp updatedTime;
	
	public DailyChart(String dailyChartId, long userId, int dailySum, int dailyCount, double dailyAverage, Timestamp createdTime, Timestamp updatedTime) {
		this.dailyChartId = dailyChartId;
		this.userId = userId;
		this.dailySum = dailySum;
		this.dailyCount = dailyCount;
		this.dailyAverage = dailyAverage;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
	}

	public DailyChart() {}

	public String getDailyChartId() {
		return dailyChartId;
	}

	public void setDailyChartId(String dailyChartId) {
		this.dailyChartId = dailyChartId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getDailySum() {
		return dailySum;
	}

	public void setDailySum(int dailySum) {
		this.dailySum = dailySum;
	}

	public int getDailyCount() {
		return dailyCount;
	}

	public void setDailyCount(int dailyCount) {
		this.dailyCount = dailyCount;
	}

	public double getDailyAverage() {
		return dailyAverage;
	}

	public void setDailyAverage(double dailyAverage) {
		this.dailyAverage = dailyAverage;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public Timestamp getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

	@Override
	public String toString() {
		return "DailyChart [dailyChartId=" + dailyChartId + ", userId=" + userId + ", dailySum=" + dailySum
				+ ", dailyCount=" + dailyCount + ", dailyAverage=" + dailyAverage + ", createdTime=" + createdTime
				+ ", updatedTime=" + updatedTime + "]";
	}
	
}
