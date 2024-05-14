package com.zerogravity.myapp.model.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DailyStatics {
	
	private String dailyStaticsId;
	private long userId;
	private int dailySum;
	private int dailyCount;
	private double dailyAverage;
	private Timestamp createdTime;
	private Timestamp updatedTime;
	
	public DailyStatics(String dailyStaticsId, long userId, int dailySum, int dailyCount, double dailyAverage, Timestamp createdTime, Timestamp updatedTime) {
		this.dailyStaticsId = dailyStaticsId;
		this.userId = userId;
		this.dailySum = dailySum;
		this.dailyCount = dailyCount;
		this.dailyAverage = dailyAverage;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
	}

	public DailyStatics() {}

	public String getDailyStaticsId() {
		return dailyStaticsId;
	}

	public void setDailyStaticsId(String dailyStaticsId) {
		this.dailyStaticsId = dailyStaticsId;
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
		return "DailyStatics [dailyStaticsId=" + dailyStaticsId + ", userId=" + userId + ", dailySum=" + dailySum
				+ ", dailyCount=" + dailyCount + ", dailyAverage=" + dailyAverage + ", createdTime=" + createdTime
				+ ", updatedTime=" + updatedTime + "]";
	}
	
}
