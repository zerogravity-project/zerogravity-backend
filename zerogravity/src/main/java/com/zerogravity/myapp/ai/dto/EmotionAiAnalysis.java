package com.zerogravity.myapp.ai.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.sql.Timestamp;
import java.util.List;

/**
 * AI emotion analysis record
 * Maps to emotion_ai_analysis table
 */
public class EmotionAiAnalysis {

	@JsonSerialize(using = ToStringSerializer.class)
	private Long analysisId;  // Snowflake ID

	@JsonSerialize(using = ToStringSerializer.class)
	private Long userId;  // Snowflake ID

	private String diaryEntry;
	private Integer suggestedEmotionId;  // Can be null if not predicted
	private String reasoning;
	private Double confidence;
	private Timestamp analyzedAt;
	private Boolean isAccepted;
	private Timestamp acceptedAt;

	@JsonSerialize(using = ToStringSerializer.class)
	private Long emotionRecordId;  // References emotion_record if accepted

	private List<String> suggestedReasons;  // Loaded from emotion_ai_analysis_reason table

	// Constructors
	public EmotionAiAnalysis() {}

	public EmotionAiAnalysis(Long analysisId, Long userId, String diaryEntry, Integer suggestedEmotionId,
							 String reasoning, Double confidence, Timestamp analyzedAt, Boolean isAccepted,
							 Timestamp acceptedAt, Long emotionRecordId, List<String> suggestedReasons) {
		this.analysisId = analysisId;
		this.userId = userId;
		this.diaryEntry = diaryEntry;
		this.suggestedEmotionId = suggestedEmotionId;
		this.reasoning = reasoning;
		this.confidence = confidence;
		this.analyzedAt = analyzedAt;
		this.isAccepted = isAccepted;
		this.acceptedAt = acceptedAt;
		this.emotionRecordId = emotionRecordId;
		this.suggestedReasons = suggestedReasons;
	}

	// Getters and Setters
	public Long getAnalysisId() {
		return analysisId;
	}

	public void setAnalysisId(Long analysisId) {
		this.analysisId = analysisId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getDiaryEntry() {
		return diaryEntry;
	}

	public void setDiaryEntry(String diaryEntry) {
		this.diaryEntry = diaryEntry;
	}

	public Integer getSuggestedEmotionId() {
		return suggestedEmotionId;
	}

	public void setSuggestedEmotionId(Integer suggestedEmotionId) {
		this.suggestedEmotionId = suggestedEmotionId;
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

	public Timestamp getAnalyzedAt() {
		return analyzedAt;
	}

	public void setAnalyzedAt(Timestamp analyzedAt) {
		this.analyzedAt = analyzedAt;
	}

	public Boolean getIsAccepted() {
		return isAccepted;
	}

	public void setIsAccepted(Boolean isAccepted) {
		this.isAccepted = isAccepted;
	}

	public Timestamp getAcceptedAt() {
		return acceptedAt;
	}

	public void setAcceptedAt(Timestamp acceptedAt) {
		this.acceptedAt = acceptedAt;
	}

	public Long getEmotionRecordId() {
		return emotionRecordId;
	}

	public void setEmotionRecordId(Long emotionRecordId) {
		this.emotionRecordId = emotionRecordId;
	}

	public List<String> getSuggestedReasons() {
		return suggestedReasons;
	}

	public void setSuggestedReasons(List<String> suggestedReasons) {
		this.suggestedReasons = suggestedReasons;
	}

	@Override
	public String toString() {
		return "EmotionAiAnalysis{" +
			"analysisId=" + analysisId +
			", userId=" + userId +
			", diaryEntry='" + (diaryEntry != null && diaryEntry.length() > 50 ? diaryEntry.substring(0, 50) + "..." : diaryEntry) + '\'' +
			", suggestedEmotionId=" + suggestedEmotionId +
			", reasoning='" + (reasoning != null && reasoning.length() > 50 ? reasoning.substring(0, 50) + "..." : reasoning) + '\'' +
			", confidence=" + confidence +
			", analyzedAt=" + analyzedAt +
			", isAccepted=" + isAccepted +
			", acceptedAt=" + acceptedAt +
			", emotionRecordId=" + emotionRecordId +
			", suggestedReasons=" + suggestedReasons +
			'}';
	}
}
