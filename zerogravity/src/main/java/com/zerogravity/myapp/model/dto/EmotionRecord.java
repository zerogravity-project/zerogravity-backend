package com.zerogravity.myapp.model.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class EmotionRecord {
	
	private String emotionRecordId;
	private long userId;
	private String emotionDetailId;
	private String diaryEntry;
	private int emotionRecordType;
	private Timestamp createdTime;
	private Timestamp updatedTime;
	
	public EmotionRecord() {}

	public EmotionRecord(String emotionRecordId, long userId, String emotionDetailId, String diaryEntry,
			int emotionRecordType, Timestamp createdTime, Timestamp updatedTime) {
		this.emotionRecordId = emotionRecordId;
		this.userId = userId;
		this.emotionDetailId = emotionDetailId;
		this.diaryEntry = diaryEntry;
		this.emotionRecordType = emotionRecordType;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
	}

	public String getEmotionRecordId() {
		return emotionRecordId;
	}

	public void setEmotionRecordId(String emotionRecordId) {
		this.emotionRecordId = emotionRecordId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
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

	public int getEmotionRecordType() {
		return emotionRecordType;
	}

	public void setEmotionRecordType(int emotionRecordType) {
		this.emotionRecordType = emotionRecordType;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public Timestamp getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

	@Override
	public String toString() {
		return "EmotionRecord [emotionRecordId=" + emotionRecordId + ", userId=" + userId + ", emotionDetailId="
				+ emotionDetailId + ", diaryEntry=" + diaryEntry + ", emotionRecordType=" + emotionRecordType
				+ ", createdTime=" + createdTime + ", updatedTime=" + updatedTime + "]";
	}

	
	
}
