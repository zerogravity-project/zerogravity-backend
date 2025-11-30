package com.zerogravity.myapp.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response DTO for diary summary
 * Contains AI-generated summary of diary entries within a period
 */
@Schema(description = "Diary Summary Response")
public class DiarySummaryResponse {

	@Schema(description = "AI-generated summary of diary entries (max 10000 characters)", example = "During this period, you mostly had positive experiences related to work and family.")
	private String summary;

	@Schema(description = "Number of diary entries used for summarization", example = "5")
	private Integer totalEntries;

	public DiarySummaryResponse() {
	}

	public DiarySummaryResponse(String summary, Integer totalEntries) {
		this.summary = summary;
		this.totalEntries = totalEntries;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Integer getTotalEntries() {
		return totalEntries;
	}

	public void setTotalEntries(Integer totalEntries) {
		this.totalEntries = totalEntries;
	}

	@Override
	public String toString() {
		return "DiarySummaryResponse [summary=" + summary + ", totalEntries=" + totalEntries + "]";
	}
}
