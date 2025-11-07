package com.zerogravity.myapp.emotion.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Create Emotion Record Request")
public class CreateEmotionRecordRequest {

	@Schema(description = "Emotion ID (0-6)", example = "5")
	private Integer emotionId;

	@Schema(description = "Emotion record type (daily or moment)", example = "daily", required = true)
	private String emotionRecordType;

	@Schema(description = "Emotion reasons list", example = "[\"Work\", \"Friends\"]")
	private List<String> emotionReasons;

	@Schema(description = "Diary content (required for AI prediction, 20-1000 characters)", example = "Great day with team")
	private String diaryEntry;

	@Schema(description = "AI analysis ID (from /ai/predict-emotion endpoint). If provided, use AI-predicted values.", example = "1234567890123456789")
	private Long aiAnalysisId;

	public CreateEmotionRecordRequest() {}

	public CreateEmotionRecordRequest(Integer emotionId, String emotionRecordType, List<String> emotionReasons, String diaryEntry, Long aiAnalysisId) {
		this.emotionId = emotionId;
		this.emotionRecordType = emotionRecordType;
		this.emotionReasons = emotionReasons;
		this.diaryEntry = diaryEntry;
		this.aiAnalysisId = aiAnalysisId;
	}

	public Integer getEmotionId() {
		return emotionId;
	}

	public void setEmotionId(Integer emotionId) {
		this.emotionId = emotionId;
	}

	public String getEmotionRecordType() {
		return emotionRecordType;
	}

	public void setEmotionRecordType(String emotionRecordType) {
		this.emotionRecordType = emotionRecordType;
	}

	public List<String> getEmotionReasons() {
		return emotionReasons;
	}

	public void setEmotionReasons(List<String> emotionReasons) {
		this.emotionReasons = emotionReasons;
	}

	public String getDiaryEntry() {
		return diaryEntry;
	}

	public void setDiaryEntry(String diaryEntry) {
		this.diaryEntry = diaryEntry;
	}

	public Long getAiAnalysisId() {
		return aiAnalysisId;
	}

	public void setAiAnalysisId(Long aiAnalysisId) {
		this.aiAnalysisId = aiAnalysisId;
	}

	@Override
	public String toString() {
		return "CreateEmotionRecordRequest [emotionId=" + emotionId + ", emotionRecordType=" + emotionRecordType
				+ ", emotionReasons=" + emotionReasons + ", diaryEntry=" + diaryEntry + ", aiAnalysisId=" + aiAnalysisId + "]";
	}
}
