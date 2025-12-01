package com.zerogravity.myapp.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

/**
 * Response for AI emotion prediction
 * Contains AI-predicted values (null if already provided by user)
 */
@Schema(description = "AI emotion prediction response")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmotionPredictionResponse {

	@Schema(description = "Analysis ID (Snowflake ID)", example = "1234567890123456789")
	private String analysisId;

	@Schema(description = "Suggested emotion ID (0-6). Null if user already provided.", example = "5")
	private Integer suggestedEmotionId;

	@Schema(description = "Suggested emotion reasons. Null if user already provided.", example = "[\"Work\", \"Self-care\"]")
	private List<String> suggestedReasons;

	@Schema(description = "AI-refined version of diary entry (max 300 chars)", example = "Had a productive day at work and took time for self-care")
	private String refinedDiary;

	@Schema(description = "Explanation of the prediction", example = "The diary entry shows professional achievement and personal satisfaction...")
	private String reasoning;

	@Schema(description = "Confidence score (0.0 - 1.0)", example = "0.92")
	private Double confidence;

	@Schema(description = "Timestamp when analysis was performed (ISO 8601 with timezone)", example = "2025-10-26T15:30:00+09:00")
	private String analyzedAt;

	// Constructors
	public EmotionPredictionResponse() {}

	public EmotionPredictionResponse(String analysisId, Integer suggestedEmotionId, List<String> suggestedReasons,
									 String refinedDiary, String reasoning, Double confidence, String analyzedAt) {
		this.analysisId = analysisId;
		this.suggestedEmotionId = suggestedEmotionId;
		this.suggestedReasons = suggestedReasons;
		this.refinedDiary = refinedDiary;
		this.reasoning = reasoning;
		this.confidence = confidence;
		this.analyzedAt = analyzedAt;
	}

	// Getters and Setters
	public String getAnalysisId() {
		return analysisId;
	}

	public void setAnalysisId(String analysisId) {
		this.analysisId = analysisId;
	}

	public Integer getSuggestedEmotionId() {
		return suggestedEmotionId;
	}

	public void setSuggestedEmotionId(Integer suggestedEmotionId) {
		this.suggestedEmotionId = suggestedEmotionId;
	}

	public List<String> getSuggestedReasons() {
		return suggestedReasons;
	}

	public void setSuggestedReasons(List<String> suggestedReasons) {
		this.suggestedReasons = suggestedReasons;
	}

	public String getRefinedDiary() {
		return refinedDiary;
	}

	public void setRefinedDiary(String refinedDiary) {
		this.refinedDiary = refinedDiary;
	}

	public String getReasoning() {
		return reasoning;
	}

	public void setReasoning(String reasoning) {
		this.reasoning = reasoning;
	}

	public Double getConfidence() {
		return confidence;
	}

	public void setConfidence(Double confidence) {
		this.confidence = confidence;
	}

	public String getAnalyzedAt() {
		return analyzedAt;
	}

	public void setAnalyzedAt(String analyzedAt) {
		this.analyzedAt = analyzedAt;
	}

	@Override
	public String toString() {
		return "EmotionPredictionResponse{" +
			"analysisId='" + analysisId + '\'' +
			", suggestedEmotionId=" + suggestedEmotionId +
			", suggestedReasons=" + suggestedReasons +
			", refinedDiary='" + (refinedDiary != null && refinedDiary.length() > 50 ? refinedDiary.substring(0, 50) + "..." : refinedDiary) + '\'' +
			", reasoning='" + reasoning + '\'' +
			", confidence=" + confidence +
			", analyzedAt='" + analyzedAt + '\'' +
			'}';
	}
}
