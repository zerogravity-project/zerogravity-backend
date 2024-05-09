package com.zerogravity.myapp.model.dto;

import java.math.BigInteger;

public class EmotionRecord {
	
	private String emotionRecordId;
	private BigInteger userId;
	private String emotionDetailId;
	private String diaryEntry;
	private String createdTime;
	private String updatedTime;
	
	public EmotionRecord() {}

	public EmotionRecord(String emotionRecordId, BigInteger userId, String emotionDetailId, String diaryEntry, String createdTime, String updatedTime) {
		this.emotionRecordId = emotionRecordId;
		this.userId = userId;
		this.emotionDetailId = emotionDetailId;
		this.diaryEntry = diaryEntry;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
	}

	public String getEmotionRecordId() {
		return emotionRecordId;
	}

	public void setEmotionRecordId(String emotionRecordId) {
		this.emotionRecordId = emotionRecordId;
	}

	public BigInteger getUserId() {
		return userId;
	}

	public void setUserId(BigInteger userId) {
		this.userId = userId;
	}

	public String getEmotionDetailId() {
		return emotionDetailId;
	}

	public void setEmotionDetailId(String emotionDetailId) {
		this.emotionDetailId = emotionDetailId;
	}

	public String getDiaryEntry() {
		return diaryEntry;
	}

	public void setDiaryEntry(String diaryEntry) {
		this.diaryEntry = diaryEntry;
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
		return "EmotionRecord [emotionRecordId=" + emotionRecordId + ", userId=" + userId + ", emotionDetailId="
				+ emotionDetailId + ", diaryEntry=" + diaryEntry + ", createdTime=" + createdTime + ", updatedTime="
				+ updatedTime + "]";
	}
	
}
