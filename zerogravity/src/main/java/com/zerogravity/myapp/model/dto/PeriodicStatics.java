package com.zerogravity.myapp.model.dto;

import java.sql.Timestamp;

public class PeriodicStatics {
	
	private String periodicStaticsId;
	private long userId;
	private Timestamp periodStart;
	private Timestamp periodEnd;
	private String periodType;
	private int periodicCount; 
	private int periodicSum;
	private double periodicAverage; 
	private Timestamp createdTime;
	private Timestamp updatedTime;
	
	public PeriodicStatics() {}

	public PeriodicStatics(String periodicStaticsId, long userId, Timestamp periodStart, Timestamp periodEnd, String periodType, int periodicCount, int periodicSum, double periodicAverage, Timestamp createdTime, Timestamp updatedTime) {
		this.periodicStaticsId = periodicStaticsId;
		this.userId = userId;
		this.periodStart = periodStart;
		this.periodEnd = periodEnd;
		this.periodType = periodType;
		this.periodicCount = periodicCount;
		this.periodicSum = periodicSum;
		this.periodicAverage = periodicAverage;
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

	public int getPeriodicCount() {
		return periodicCount;
	}

	public void setPeriodicCount(int periodicCount) {
		this.periodicCount = periodicCount;
	}

	public int getPeriodicSum() {
		return periodicSum;
	}

	public void setPeriodicSum(int periodicSum) {
		this.periodicSum = periodicSum;
	}

	public double getPeriodicAverage() {
		return periodicAverage;
	}

	public void setPeriodicAverage(double periodicAverage) {
		this.periodicAverage = periodicAverage;
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
				+ periodStart + ", periodEnd=" + periodEnd + ", periodType=" + periodType + ", periodicCount="
				+ periodicCount + ", periodicSum=" + periodicSum + ", periodicAverage=" + periodicAverage
				+ ", createdTime=" + createdTime + ", updatedTime=" + updatedTime + "]";
	}

}
