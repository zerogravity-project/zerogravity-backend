package com.zerogravity.myapp.ai.service;

import java.util.List;

/**
 * Result from AI emotion prediction
 * Contains predicted values for missing information
 * null values indicate that field was provided by user
 */
public class EmotionPredictionResult {

	private Integer emotionId;  // null if user provided
	private List<String> reasons;  // null if user provided
	private String refinedDiary;  // AI-refined version of diary entry (max 300 chars)
	private String reasoning;
	private Double confidence;

	// Constructors
	public EmotionPredictionResult() {}

	public EmotionPredictionResult(Integer emotionId, List<String> reasons, String refinedDiary, String reasoning, Double confidence) {
		this.emotionId = emotionId;
		this.reasons = reasons;
		this.refinedDiary = refinedDiary;
		this.reasoning = reasoning;
		this.confidence = confidence;
	}

	// Getters and Setters
	public Integer getEmotionId() {
		return emotionId;
	}

	public void setEmotionId(Integer emotionId) {
		this.emotionId = emotionId;
	}

	public List<String> getReasons() {
		return reasons;
	}

	public void setReasons(List<String> reasons) {
		this.reasons = reasons;
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

	@Override
	public String toString() {
		return "EmotionPredictionResult{" +
			"emotionId=" + emotionId +
			", reasons=" + reasons +
			", refinedDiary='" + (refinedDiary != null && refinedDiary.length() > 50 ? refinedDiary.substring(0, 50) + "..." : refinedDiary) + '\'' +
			", reasoning='" + reasoning + '\'' +
			", confidence=" + confidence +
			'}';
	}
}
