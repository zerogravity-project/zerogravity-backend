package com.zerogravity.myapp.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "감정 기록 생성 요청")
public class CreateEmotionRecordRequest {

	@Schema(description = "감정 ID (0-6)", example = "5", required = true)
	private Integer emotionId;

	@Schema(description = "감정 기록 타입 (daily 또는 moment)", example = "daily", required = true)
	private String emotionRecordType;

	@Schema(description = "감정 이유 목록", example = "[\"Work\", \"Friends\"]", required = true)
	private List<String> emotionReasons;

	@Schema(description = "다이어리 내용 (nullable)", example = "Great day with team")
	private String diaryEntry;

	public CreateEmotionRecordRequest() {}

	public CreateEmotionRecordRequest(Integer emotionId, String emotionRecordType, List<String> emotionReasons, String diaryEntry) {
		this.emotionId = emotionId;
		this.emotionRecordType = emotionRecordType;
		this.emotionReasons = emotionReasons;
		this.diaryEntry = diaryEntry;
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

	@Override
	public String toString() {
		return "CreateEmotionRecordRequest [emotionId=" + emotionId + ", emotionRecordType=" + emotionRecordType
				+ ", emotionReasons=" + emotionReasons + ", diaryEntry=" + diaryEntry + "]";
	}
}
