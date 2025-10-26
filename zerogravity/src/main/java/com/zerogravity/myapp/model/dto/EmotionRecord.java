package com.zerogravity.myapp.model.dto;

import java.sql.Timestamp;
import java.util.List;

/**
 * Emotion record information
 * Represents a user's emotion record with reasons and diary entry
 */
public class EmotionRecord {

	private String emotionRecordId;
	private long userId;
	private int emotionId;
	private EmotionRecord.Type emotionRecordType;
	private List<String> emotionReasons;
	private String diaryEntry;
	private Timestamp createdTime;
	private Timestamp updatedTime;

	// Nested Enums

	/**
	 * Emotion record type enumeration
	 * Represents the type of emotion record (daily or moment)
	 */
	public enum Type {
		DAILY("daily"),
		MOMENT("moment");

		private final String value;

		Type(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public static Type fromValue(String value) {
			for (Type type : Type.values()) {
				if (type.value.equalsIgnoreCase(value)) {
					return type;
				}
			}
			throw new IllegalArgumentException("Unknown emotion record type: " + value);
		}
	}

	/**
	 * Emotion reason enumeration
	 * Represents reasons for emotions
	 */
	public enum Reason {
		HEALTH("Health"),
		FITNESS("Fitness"),
		SELF_CARE("Self-care"),
		HOBBY("Hobby"),
		IDENTITY("Identity"),
		RELIGION("Religion"),
		COMMUNITY("Community"),
		FAMILY("Family"),
		FRIENDS("Friends"),
		PARTNER("Partner"),
		ROMANCE("Romance"),
		MONEY("Money"),
		HOUSEWORK("Housework"),
		WORK("Work"),
		EDUCATION("Education"),
		TRAVEL("Travel"),
		WEATHER("Weather"),
		DOMESTIC_ISSUES("Domestic Issues"),
		GLOBAL_ISSUES("Global Issues");

		private final String displayName;

		Reason(String displayName) {
			this.displayName = displayName;
		}

		public String getDisplayName() {
			return displayName;
		}

		public static Reason fromDisplayName(String displayName) {
			for (Reason reason : Reason.values()) {
				if (reason.displayName.equalsIgnoreCase(displayName)) {
					return reason;
				}
			}
			throw new IllegalArgumentException("Unknown emotion reason: " + displayName);
		}
	}

	// Constructors

	public EmotionRecord() {}

	public EmotionRecord(String emotionRecordId, long userId, int emotionId, EmotionRecord.Type emotionRecordType, List<String> emotionReasons, String diaryEntry, Timestamp createdTime, Timestamp updatedTime) {
		this.emotionRecordId = emotionRecordId;
		this.userId = userId;
		this.emotionId = emotionId;
		this.emotionRecordType = emotionRecordType;
		this.emotionReasons = emotionReasons;
		this.diaryEntry = diaryEntry;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
	}

	// Getters and Setters

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

	public int getEmotionId() {
		return emotionId;
	}

	public void setEmotionId(int emotionId) {
		this.emotionId = emotionId;
	}

	public EmotionRecord.Type getEmotionRecordType() {
		return emotionRecordType;
	}

	public void setEmotionRecordType(EmotionRecord.Type emotionRecordType) {
		this.emotionRecordType = emotionRecordType;
	}

	public List<String> getEmotionReasons() {
		return emotionReasons;
	}

	public void setEmotionReasons(List<String> emotionReasons) {
		this.emotionReasons = emotionReasons;
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
		return "EmotionRecord [emotionRecordId=" + emotionRecordId + ", userId=" + userId + ", emotionId="
				+ emotionId + ", emotionRecordType=" + emotionRecordType + ", emotionReasons=" + emotionReasons
				+ ", diaryEntry=" + diaryEntry + ", createdTime=" + createdTime + ", updatedTime=" + updatedTime + "]";
	}

}
