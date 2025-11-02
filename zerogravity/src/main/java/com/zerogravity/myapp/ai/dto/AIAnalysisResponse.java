package com.zerogravity.myapp.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * API response for AI emotion analysis summary
 * Returns period information, AI-generated summary, and generation timestamp
 */
@Schema(description = "AI emotion analysis summary response")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AIAnalysisResponse {

	@Schema(description = "Analysis period type", example = "week")
	private String period;

	@Schema(description = "Period start date (ISO 8601)", example = "2025-10-26")
	private String startDate;

	@Schema(description = "Period end date (ISO 8601)", example = "2025-11-01")
	private String endDate;

	@Schema(description = "AI-generated summary data")
	private SummaryData summary;

	@Schema(description = "Timestamp when analysis was generated (ISO 8601 with timezone)", example = "2025-10-26T15:30:00+09:00")
	private String generatedAt;

	// Constructors
	public AIAnalysisResponse() {}

	public AIAnalysisResponse(String period, String startDate, String endDate, SummaryData summary, String generatedAt) {
		this.period = period;
		this.startDate = startDate;
		this.endDate = endDate;
		this.summary = summary;
		this.generatedAt = generatedAt;
	}

	// Getters and Setters
	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public SummaryData getSummary() {
		return summary;
	}

	public void setSummary(SummaryData summary) {
		this.summary = summary;
	}

	public String getGeneratedAt() {
		return generatedAt;
	}

	public void setGeneratedAt(String generatedAt) {
		this.generatedAt = generatedAt;
	}

	@Override
	public String toString() {
		return "AIAnalysisResponse{" +
			"period='" + period + '\'' +
			", startDate='" + startDate + '\'' +
			", endDate='" + endDate + '\'' +
			", summary=" + summary +
			", generatedAt='" + generatedAt + '\'' +
			'}';
	}
}
