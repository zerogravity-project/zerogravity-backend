package com.zerogravity.myapp.emotion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "Create Emotion Record Request")
public class CreateEmotionRecordRequest {

	@Schema(description = "Emotion ID (0-6)", example = "5")
	private Integer emotionId;

	@Schema(description = "Emotion record type (daily or moment)", example = "daily", required = true)
	private String emotionRecordType;

	@Schema(description = "Emotion reasons list", example = "[\"Work\", \"Friends\"]")
	private List<String> emotionReasons;

	@Schema(description = "Diary content (max 300 characters)", example = "Great day with team")
	@Size(max = 300, message = "Diary entry must not exceed 300 characters")
	private String diaryEntry;

	@Schema(description = "AI analysis ID (from /ai/predict-emotion endpoint). If provided, use AI-predicted values.", example = "1234567890123456789")
	private Long aiAnalysisId;

	@Schema(description = "Optional record date (ISO Date format: YYYY-MM-DD). If not provided, current date/time is used. Past dates are saved at 12:00:00 (noon). Future dates are not allowed.", example = "2025-11-05")
	private String recordDate;

	public CreateEmotionRecordRequest() {}

	public CreateEmotionRecordRequest(Integer emotionId, String emotionRecordType, List<String> emotionReasons, String diaryEntry, Long aiAnalysisId, String recordDate) {
		this.emotionId = emotionId;
		this.emotionRecordType = emotionRecordType;
		this.emotionReasons = emotionReasons;
		this.diaryEntry = diaryEntry;
		this.aiAnalysisId = aiAnalysisId;
		this.recordDate = recordDate;
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

	public String getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}

	@Override
	public String toString() {
		return "CreateEmotionRecordRequest [emotionId=" + emotionId + ", emotionRecordType=" + emotionRecordType
				+ ", emotionReasons=" + emotionReasons + ", diaryEntry=" + diaryEntry + ", aiAnalysisId=" + aiAnalysisId + ", recordDate=" + recordDate + "]";
	}
}
