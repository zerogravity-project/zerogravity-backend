package com.zerogravity.myapp.model.dto;

import java.sql.Timestamp;

public class EmotionRecord {
	
	private String emotionRecordId;
	private long userId;
	private String emotionReason;
	private String emotionRecordType;
	private int emotionRecordLevel;
	private String emotionRecordState;
	private String diaryEntry;
	private Timestamp createdTime;
	private Timestamp updatedTime;
	
	public EmotionRecord() {}

	public EmotionRecord(String emotionRecordId, long userId, String emotionReason, String emotionRecordType, int emotionRecordLevel, String emotionRecordState, String diaryEntry, Timestamp createdTime, Timestamp updatedTime) {
		this.emotionRecordId = emotionRecordId;
		this.userId = userId;
		this.emotionReason = emotionReason;
		this.emotionRecordType = emotionRecordType;
		this.emotionRecordLevel = emotionRecordLevel;
		this.emotionRecordState = emotionRecordState;
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

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getEmotionReason() {
		return emotionReason;
	}

	public void setEmotionReason(String emotionReason) {
		this.emotionReason = emotionReason;
	}

	public String getEmotionRecordType() {
		return emotionRecordType;
	}

	public void setEmotionRecordType(String emotionRecordType) {
		this.emotionRecordType = emotionRecordType;
	}

	public int getEmotionRecordLevel() {
		return emotionRecordLevel;
	}

	public void setEmotionRecordLevel(int emotionRecordLevel) {
		this.emotionRecordLevel = emotionRecordLevel;
	}

	public String getEmotionRecordState() {
		return emotionRecordState;
	}

	public void setEmotionRecordState(String emotionRecordState) {
		this.emotionRecordState = emotionRecordState;
	}

	public String getDiaryEntry() {
		return diaryEntry;
	}

	public void setDiaryEntry(String diaryEntry) {
		this.diaryEntry = diaryEntry;
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
		return "EmotionRecord [emotionRecordId=" + emotionRecordId + ", userId=" + userId + ", emotionReason="
				+ emotionReason + ", emotionRecordType=" + emotionRecordType + ", emotionRecordLevel="
				+ emotionRecordLevel + ", emotionRecordState=" + emotionRecordState + ", diaryEntry=" + diaryEntry
				+ ", createdTime=" + createdTime + ", updatedTime=" + updatedTime + "]";
	}

}
