package com.zerogravity.myapp.model.dto;

public class PeriodicStatics {
	
	private String periodicStaticsId;
	private long userId;
	private String periodEnd;
	private String periodType;
	private int count; 
	private int sumScore;
	private double averageScore; 
	private String createdTime;
	private String updatedTime;
	
	public PeriodicStatics() {}

	public PeriodicStatics(String periodicStaticsId, long userId, String periodEnd, String periodType, int sumScore, int count, double averageScore, String createdTime, String updatedTime) {
		this.periodicStaticsId = periodicStaticsId;
		this.userId = userId;
		this.periodEnd = periodEnd;
		this.periodType = periodType;
		this.count = count;
		this.sumScore = sumScore;
		this.averageScore = averageScore;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
	}

	public String getPeriodicStaticsId() {
		return periodicStaticsId;
	}

	public void setPeriodicStaticsId(String periodicStaticsId) {
		this.periodicStaticsId = periodicStaticsId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getPeriodEnd() {
		return periodEnd;
	}

	public void setPeriodEnd(String periodEnd) {
		this.periodEnd = periodEnd;
	}

	public String getPeriodType() {
		return periodType;
	}

	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getSumScore() {
		return sumScore;
	}

	public void setSumScore(int sumScore) {
		this.sumScore = sumScore;
	}

	public double getAverageScore() {
		return averageScore;
	}

	public void setAverageScore(double averageScore) {
		this.averageScore = averageScore;
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
		return "PeriodicStatics [periodicStaticsId=" + periodicStaticsId + ", userId=" + userId + ", periodEnd="
				+ periodEnd + ", periodType=" + periodType + ", count=" + count + ", sumScore=" + sumScore
				+ ", averageScore=" + averageScore + ", createdTime=" + createdTime + ", updatedTime=" + updatedTime
				+ "]";
	}

}
