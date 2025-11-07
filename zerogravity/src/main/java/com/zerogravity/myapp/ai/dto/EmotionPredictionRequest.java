package com.zerogravity.myapp.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * Request for AI emotion prediction
 * Users can provide partial information (emotionId or emotionReasons)
 * AI will predict the missing parts based on diary entry
 *
 * - emotionId = null && emotionReasons = null → predict both
 * - emotionId = 5 && emotionReasons = null → predict reasons only
 * - emotionId = null && emotionReasons = [...] → predict emotionId only
 * - emotionId = 5 && emotionReasons = [...] → error (nothing to predict)
 */
@Schema(description = "Request for AI emotion prediction")
public class EmotionPredictionRequest {

	@Schema(description = "Diary entry for emotion analysis", example = "Today was a great day. My team appreciated my work.")
	private String diaryEntry;

	@Schema(description = "Optional: User-provided emotion ID (0-6). If provided, AI will only predict reasons.", example = "5")
	private Integer emotionId;

	@Schema(description = "Optional: User-provided emotion reasons. If provided, AI will only predict emotion ID.", example = "[\"Work\", \"Self-care\"]")
	private List<String> emotionReasons;

	// Constructors
	public EmotionPredictionRequest() {}

	public EmotionPredictionRequest(String diaryEntry, Integer emotionId, List<String> emotionReasons) {
		this.diaryEntry = diaryEntry;
		this.emotionId = emotionId;
		this.emotionReasons = emotionReasons;
	}

	// Getters and Setters
	public String getDiaryEntry() {
		return diaryEntry;
	}

	public void setDiaryEntry(String diaryEntry) {
		this.diaryEntry = diaryEntry;
	}

	public Integer getEmotionId() {
		return emotionId;
	}

	public void setEmotionId(Integer emotionId) {
		this.emotionId = emotionId;
	}

	public List<String> getEmotionReasons() {
		return emotionReasons;
	}

	public void setEmotionReasons(List<String> emotionReasons) {
		this.emotionReasons = emotionReasons;
	}

	@Override
	public String toString() {
		return "EmotionPredictionRequest{" +
			"diaryEntry='" + diaryEntry + '\'' +
			", emotionId=" + emotionId +
			", emotionReasons=" + emotionReasons +
			'}';
	}
}
