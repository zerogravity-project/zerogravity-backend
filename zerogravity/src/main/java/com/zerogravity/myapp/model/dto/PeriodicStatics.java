package com.zerogravity.myapp.model.dto;

import java.sql.Timestamp;

public class PeriodicStatics {
	
	private String periodicStaticsId;
	private long userId;
	private Timestamp periodStart;
	private Timestamp periodEnd;
	private String periodType;
	private int count; 
	private int sumScore;
	private double averageScore; 
	private Timestamp createdTime;
	private Timestamp updatedTime;
	
	public PeriodicStatics() {}

	public PeriodicStatics(String periodicStaticsId, long userId, Timestamp periodStart, Timestamp periodEnd, String periodType, int count, int sumScore, double averageScore, Timestamp createdTime, Timestamp updatedTime) {
		this.periodicStaticsId = periodicStaticsId;
		this.userId = userId;
		this.periodStart = periodStart;
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

	public Timestamp getPeriodStart() {
		return periodStart;
	}

	public void setPeriodStart(Timestamp periodStart) {
		this.periodStart = periodStart;
	}

	public Timestamp getPeriodEnd() {
		return periodEnd;
	}

	public void setPeriodEnd(Timestamp periodEnd) {
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
		return "PeriodicStatics [periodicStaticsId=" + periodicStaticsId + ", userId=" + userId + ", periodStart="
				+ periodStart + ", periodEnd=" + periodEnd + ", periodType=" + periodType + ", count=" + count
				+ ", sumScore=" + sumScore + ", averageScore=" + averageScore + ", createdTime=" + createdTime
				+ ", updatedTime=" + updatedTime + "]";
	}

	
}
