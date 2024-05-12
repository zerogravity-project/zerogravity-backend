package com.zerogravity.myapp.model.dto;

public class DailyStatics {
	
	private String dailyStaticsId;
	private long userId;
	private String aggregatedDate;
	private int dailySum;
	
	public DailyStatics() {}

	public DailyStatics(String dailyStaticsId, long userId, String aggregatedDate, int dailySum) {
		this.dailyStaticsId = dailyStaticsId;
		this.userId = userId;
		this.aggregatedDate = aggregatedDate;
		this.dailySum = dailySum;
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

	public String getAggregatedDate() {
		return aggregatedDate;
	}

	public void setAggregatedDate(String aggregatedDate) {
		this.aggregatedDate = aggregatedDate;
	}

	public int getDailySum() {
		return dailySum;
	}

	public void setDailySum(int dailySum) {
		this.dailySum = dailySum;
	}

	@Override
	public String toString() {
		return "DailyStatics [dailyStaticsId=" + dailyStaticsId + ", userId=" + userId + ", aggregatedDate="
				+ aggregatedDate + ", dailySum=" + dailySum + "]";
	}
	
}
