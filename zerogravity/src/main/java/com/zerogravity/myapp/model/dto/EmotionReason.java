package com.zerogravity.myapp.model.dto;

public class EmotionReason {
	private String emotionReasonId;
	private String emotionReason;
	private String createdTime;
	private String updatedTime;
	
	public EmotionReason() {}

	public EmotionReason(String emotionReasonId, String emotionReason, String createdTime, String updatedTime) {
		this.emotionReasonId = emotionReasonId;
		this.emotionReason = emotionReason;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
	}

	public String getEmotionReasonId() {
		return emotionReasonId;
	}

	public void setEmotionReasonId(String emotionReasonId) {
		this.emotionReasonId = emotionReasonId;
	}

	public String getEmotionReason() {
		return emotionReason;
	}

	public void setEmotionReason(String emotionReason) {
		this.emotionReason = emotionReason;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

	@Override
	public String toString() {
		return "EmotionReason [emotionReasonId=" + emotionReasonId +  ", emotionReason="
				+ emotionReason + ", createdTime=" + createdTime + ", updatedTime=" + updatedTime + "]";
	}
	
}
