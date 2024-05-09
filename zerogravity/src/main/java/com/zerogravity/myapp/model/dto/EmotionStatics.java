package com.zerogravity.myapp.model.dto;

import java.math.BigInteger;

public class EmotionStatics {
	
	private String emotionStaticsId;
	private long userId;
	private double avgLevelWeekly;
	private double avgLevelMonthly;
	private double avgLevelYearly;
	private String createdTime;
	private String updatedTime;
	
	public EmotionStatics() {}
	
	public EmotionStatics(String emotionStaticsId, long userId, double avgLevelWeekly, double avgLevelMonthly, double avgLevelYearly, String createdTime, String updatedTime) {
		this.emotionStaticsId = emotionStaticsId;
		this.userId = userId;
		this.avgLevelWeekly = avgLevelWeekly;
		this.avgLevelMonthly = avgLevelMonthly;
		this.avgLevelYearly = avgLevelYearly;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
	}

	public String getEmotionStaticsId() {
		return emotionStaticsId;
	}

	public void setEmotionStaticsId(String emotionStaticsId) {
		this.emotionStaticsId = emotionStaticsId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public double getAvgLevelWeekly() {
		return avgLevelWeekly;
	}

	public void setAvgLevelWeekly(double avgLevelWeekly) {
		this.avgLevelWeekly = avgLevelWeekly;
	}

	public double getAvgLevelMonthly() {
		return avgLevelMonthly;
	}

	public void setAvgLevelMonthly(double avgLevelMonthly) {
		this.avgLevelMonthly = avgLevelMonthly;
	}

	public double getAvgLevelYearly() {
		return avgLevelYearly;
	}

	public void setAvgLevelYearly(double avgLevelYearly) {
		this.avgLevelYearly = avgLevelYearly;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

	@Override
	public String toString() {
		return "EmotionStatics [emotionStaticsId=" + emotionStaticsId + ", userId=" + userId + ", avgLevelWeekly="
				+ avgLevelWeekly + ", avgLevelMonthly=" + avgLevelMonthly + ", avgLevelYearly=" + avgLevelYearly
				+ ", createdTime=" + createdTime + ", updatedTime=" + updatedTime + "]";
	}
	
}
