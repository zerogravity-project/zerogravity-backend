package com.zerogravity.myapp.emotion.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Emotion Records Response (separated by daily/moment)")
public class GetEmotionRecordsResponse {

	@Schema(description = "Daily type emotion records list")
	private List<EmotionRecordDetail> daily;

	@Schema(description = "Moment type emotion records list")
	private List<EmotionRecordDetail> moment;

	public GetEmotionRecordsResponse() {}

	public GetEmotionRecordsResponse(List<EmotionRecordDetail> daily, List<EmotionRecordDetail> moment) {
		this.daily = daily;
		this.moment = moment;
	}

	public List<EmotionRecordDetail> getDaily() {
		return daily;
	}

	public void setDaily(List<EmotionRecordDetail> daily) {
		this.daily = daily;
	}

	public List<EmotionRecordDetail> getMoment() {
		return moment;
	}

	public void setMoment(List<EmotionRecordDetail> moment) {
		this.moment = moment;
	}

	@Schema(description = "Emotion record details")
	public static class EmotionRecordDetail {
		@Schema(description = "Emotion record ID (Snowflake ID, String)", example = "1234567890123456789")
		private String emotionRecordId;

		@Schema(description = "Emotion ID (0-6)", example = "5")
		private Integer emotionId;

		@Schema(description = "Emotion Type", example = "POSITIVE")
		private String emotionType;

		@Schema(description = "Emotion reasons list", example = "[\"Health\", \"Family\"]")
		private List<String> reasons;

		@Schema(description = "Diary content", example = "Meeting went well")
		private String diaryEntry;

		@Schema(description = "Created time (ISO 8601 with offset)", example = "2025-10-26T10:30:00+09:00")
		private String createdAt;

		public EmotionRecordDetail() {}

		public EmotionRecordDetail(String emotionRecordId, Integer emotionId, String emotionType, List<String> reasons, String diaryEntry, String createdAt) {
			this.emotionRecordId = emotionRecordId;
			this.emotionId = emotionId;
			this.emotionType = emotionType;
			this.reasons = reasons;
			this.diaryEntry = diaryEntry;
			this.createdAt = createdAt;
		}

		public String getEmotionRecordId() {
			return emotionRecordId;
		}

		public void setEmotionRecordId(String emotionRecordId) {
			this.emotionRecordId = emotionRecordId;
		}

		public Integer getEmotionId() {
			return emotionId;
		}

		public void setEmotionId(Integer emotionId) {
			this.emotionId = emotionId;
		}

		public String getEmotionType() {
			return emotionType;
		}

		public void setEmotionType(String emotionType) {
			this.emotionType = emotionType;
		}

		public List<String> getReasons() {
			return reasons;
		}

		public void setReasons(List<String> reasons) {
			this.reasons = reasons;
		}

		public String getDiaryEntry() {
			return diaryEntry;
		}

		public void setDiaryEntry(String diaryEntry) {
			this.diaryEntry = diaryEntry;
		}

		public String getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(String createdAt) {
			this.createdAt = createdAt;
		}
	}
}
