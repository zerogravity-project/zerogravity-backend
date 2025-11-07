package com.zerogravity.myapp.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response DTO for diary summary
 * Contains AI-generated summary of diary entries within a period
 */
@Schema(description = "Diary Summary Response")
public class DiarySummaryResponse {

	@Schema(description = "AI-generated summary of diary entries (max 1000 characters)", example = "이 기간 동안 주로 업무와 가족 관련 긍정적인 경험이 많았습니다.")
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
