package com.zerogravity.myapp.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "감정 기록 조회 응답 (daily/moment 분리)")
public class GetEmotionRecordsResponse {

	@Schema(description = "Daily 타입 감정 기록 목록")
	private List<EmotionRecordDetail> daily;

	@Schema(description = "Moment 타입 감정 기록 목록")
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

	@Schema(description = "감정 기록 상세 정보")
	public static class EmotionRecordDetail {
		@Schema(description = "감정 기록 ID (Snowflake ID, String)", example = "1234567890123456789")
		private String emotionRecordId;

		@Schema(description = "감정 ID (0-6)", example = "5")
		private Integer emotionId;

		@Schema(description = "감정 타입", example = "POSITIVE")
		private String emotionType;

		@Schema(description = "감정 이유 목록", example = "[\"Health\", \"Family\"]")
		private List<String> reasons;

		@Schema(description = "다이어리 내용", example = "회의 잘 풀렸다")
		private String diaryEntry;

		@Schema(description = "생성 시간 (ISO 8601 with offset)", example = "2025-10-26T10:30:00+09:00")
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
