package com.zerogravity.myapp.ai.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.time.LocalDate;
import java.sql.Timestamp;

/**
 * Cache entry for AI analysis summary
 * Maps to ai_analysis_cache table
 */
public class AIAnalysisCache {

	@JsonSerialize(using = ToStringSerializer.class)
	private Long cacheId;  // Snowflake ID

	@JsonSerialize(using = ToStringSerializer.class)
	private Long userId;  // Snowflake ID

	private String period;  // WEEK, MONTH, YEAR
	private LocalDate startDate;
	private LocalDate endDate;
	private String summaryJson;  // JSON string of SummaryData
	private Timestamp generatedAt;
	private Timestamp expiresAt;

	// Constructors
	public AIAnalysisCache() {}

	public AIAnalysisCache(Long cacheId, Long userId, String period, LocalDate startDate, LocalDate endDate,
						  String summaryJson, Timestamp generatedAt, Timestamp expiresAt) {
		this.cacheId = cacheId;
		this.userId = userId;
		this.period = period;
		this.startDate = startDate;
		this.endDate = endDate;
		this.summaryJson = summaryJson;
		this.generatedAt = generatedAt;
		this.expiresAt = expiresAt;
	}

	// Getters and Setters
	public Long getCacheId() {
		return cacheId;
	}

	public void setCacheId(Long cacheId) {
		this.cacheId = cacheId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getSummaryJson() {
		return summaryJson;
	}

	public void setSummaryJson(String summaryJson) {
		this.summaryJson = summaryJson;
	}

	public Timestamp getGeneratedAt() {
		return generatedAt;
	}

	public void setGeneratedAt(Timestamp generatedAt) {
		this.generatedAt = generatedAt;
	}

	public Timestamp getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Timestamp expiresAt) {
		this.expiresAt = expiresAt;
	}

	@Override
	public String toString() {
		return "AIAnalysisCache{" +
			"cacheId=" + cacheId +
			", userId=" + userId +
			", period='" + period + '\'' +
			", startDate=" + startDate +
			", endDate=" + endDate +
			", generatedAt=" + generatedAt +
			", expiresAt=" + expiresAt +
			'}';
	}
}
