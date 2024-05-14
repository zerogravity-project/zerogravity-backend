package com.zerogravity.myapp.model.dto;

import java.time.LocalDateTime;

public class Emotion {
	
	private String emotionId;
	private String emotionType;
	private int emotionLevel;
	private String colorScheme;
	private String shapeName;
	private LocalDateTime createdTime;
	private LocalDateTime updatedTime;
	
	public Emotion() {}

	public Emotion(String emotionId, String emotionType, int emotionLevle, String colorScheme, String shapeName, LocalDateTime createdTime, LocalDateTime updatedTime) {
		this.emotionId = emotionId;
		this.emotionType = emotionType;
		this.emotionLevel = emotionLevle;
		this.colorScheme = colorScheme;
		this.shapeName = shapeName;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
	}

	public String getEmotionId() {
		return emotionId;
	}

	public void setEmotionId(String emotionId) {
		this.emotionId = emotionId;
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

	public String getColorScheme() {
		return colorScheme;
	}

	public void setColorScheme(String colorScheme) {
		this.colorScheme = colorScheme;
	}

	public String getShapeName() {
		return shapeName;
	}

	public void setShapeName(String shapeName) {
		this.shapeName = shapeName;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public LocalDateTime getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(LocalDateTime updatedTime) {
		this.updatedTime = updatedTime;
	}

	@Override
	public String toString() {
		return "Emotion [emotionId=" + emotionId + ", emotionType=" + emotionType + ", emotionLevel=" + emotionLevel
				+ ", colorScheme=" + colorScheme + ", shapeName=" + shapeName + ", createdTime=" + createdTime
				+ ", updatedTime=" + updatedTime + "]";
	}
	
	
	
}
