package com.zerogravity.myapp.emotion.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Update Emotion Record Request")
public class UpdateEmotionRecordRequest {

	@Schema(description = "Emotion ID (0-6)", example = "6", required = true)
	private Integer emotionId;

	@Schema(description = "Emotion reasons list", example = "[\"Friends\", \"Hobby\", \"Family\"]", required = true)
	private List<String> emotionReasons;

	@Schema(description = "Diary content (nullable)", example = "Updated diary entry")
	private String diaryEntry;

	public UpdateEmotionRecordRequest() {}

	public UpdateEmotionRecordRequest(Integer emotionId, List<String> emotionReasons, String diaryEntry) {
		this.emotionId = emotionId;
		this.emotionReasons = emotionReasons;
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

	public String getDiaryEntry() {
		return diaryEntry;
	}

	public void setDiaryEntry(String diaryEntry) {
		this.diaryEntry = diaryEntry;
	}

	@Override
	public String toString() {
		return "UpdateEmotionRecordRequest [emotionId=" + emotionId + ", emotionReasons=" + emotionReasons
				+ ", diaryEntry=" + diaryEntry + "]";
	}
}
