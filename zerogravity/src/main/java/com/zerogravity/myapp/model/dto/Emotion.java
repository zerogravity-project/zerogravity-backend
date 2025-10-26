package com.zerogravity.myapp.model.dto;

import java.sql.Timestamp;

/**
 * Emotion type information
 * Represents a predefined emotion type with ID (0-6), key, and display name
 */
public class Emotion {

	private int emotionId;
	private String emotionKey;
	private String emotionType;
	private int emotionLevel;
	private Timestamp createdTime;
	private Timestamp updatedTime;

	public Emotion() {}

	public Emotion(int emotionId, String emotionKey, String emotionType, int emotionLevel, Timestamp createdTime, Timestamp updatedTime) {
		this.emotionId = emotionId;
		this.emotionKey = emotionKey;
		this.emotionType = emotionType;
		this.emotionLevel = emotionLevel;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
	}

	public int getEmotionId() {
		return emotionId;
	}

	public void setEmotionId(int emotionId) {
		this.emotionId = emotionId;
	}

	public String getEmotionKey() {
		return emotionKey;
	}

	public void setEmotionKey(String emotionKey) {
		this.emotionKey = emotionKey;
	}

	public String getEmotionType() {
		return emotionType;
	}

	public void setEmotionType(String emotionType) {
		this.emotionType = emotionType;
	}

	public int getEmotionLevel() {
		return emotionLevel;
	}

	public void setEmotionLevel(int emotionLevel) {
		this.emotionLevel = emotionLevel;
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
		return "Emotion [emotionId=" + emotionId + ", emotionKey=" + emotionKey + ", emotionType=" + emotionType
				+ ", emotionLevel=" + emotionLevel + ", createdTime=" + createdTime + ", updatedTime=" + updatedTime + "]";
	}

}
