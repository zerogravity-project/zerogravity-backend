package com.zerogravity.myapp.model.dto;

public class EmotionDetail {
	private String emotionDetailId;
	private String emotionId;
	private String emotionDetail;
	private String createdTime;
	private String updatedTime;
	
	public EmotionDetail() {}

	public EmotionDetail(String emotionDetailId, String emotionId, String emotionDetail, String createdTime, String updatedTime) {
		this.emotionDetailId = emotionDetailId;
		this.emotionId = emotionId;
		this.emotionDetail = emotionDetail;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
	}

	public String getEmotionDetailId() {
		return emotionDetailId;
	}

	public void setEmotionDetailId(String emotionDetailId) {
		this.emotionDetailId = emotionDetailId;
	}

	public String getEmotionId() {
		return emotionId;
	}

	public void setEmotionId(String emotionId) {
		this.emotionId = emotionId;
	}

	public String getEmotionDetail() {
		return emotionDetail;
	}

	public void setEmotionDetail(String emotionDetail) {
		this.emotionDetail = emotionDetail;
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
		return "EmotionDetail [emotionDetailId=" + emotionDetailId + ", emotionId=" + emotionId + ", emotionDetail="
				+ emotionDetail + ", createdTime=" + createdTime + ", updatedTime=" + updatedTime + "]";
	}
	
}
