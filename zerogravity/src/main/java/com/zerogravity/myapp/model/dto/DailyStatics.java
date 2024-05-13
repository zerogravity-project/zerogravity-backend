package com.zerogravity.myapp.model.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DailyStatics {
	
	private String dailyStaticsId;
	private long userId;
	private int dailySum;
	private Timestamp createdTime;
	private Timestamp updatedTime;
	
	public DailyStatics() {}

	public DailyStatics(String dailyStaticsId, long userId, int dailySum, Timestamp createdTime, Timestamp updatedTime) {
		this.dailyStaticsId = dailyStaticsId;
		this.userId = userId;
		this.dailySum = dailySum;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
	}

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
				+ ", createdTime=" + createdTime + ", updatedTime=" + updatedTime + "]";
	}

	
}
