package com.zerogravity.myapp.model.dto;

public class Emotion {
	
	private String emotionId;
	private String emotionType;
	private int emotionLevle;
	private String colorScheme;
	private String shapeName;
	private String createdTime;
	private String updatedTime;
	
	public Emotion() {}

	public Emotion(String emotionId, String emotionType, int emotionLevle, String colorScheme, String shapeName, String createdTime, String updatedTime) {
		this.emotionId = emotionId;
		this.emotionType = emotionType;
		this.emotionLevle = emotionLevle;
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

	public int getEmotionLevle() {
		return emotionLevle;
	}

	public void setEmotionLevle(int emotionLevle) {
		this.emotionLevle = emotionLevle;
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
		return "Emotion [emotionId=" + emotionId + ", emotionType=" + emotionType + ", emotionLevle=" + emotionLevle
				+ ", colorScheme=" + colorScheme + ", shapeName=" + shapeName + ", createdTime=" + createdTime
				+ ", updatedTime=" + updatedTime + "]";
	}
	
	
	
}
