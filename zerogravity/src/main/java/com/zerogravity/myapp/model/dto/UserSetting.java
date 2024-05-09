package com.zerogravity.myapp.model.dto;

public class UserSetting {
	private String userSettingId;
	private long userId;
	private String sceneObjectId;
	private String fontStyle;
	private String colorScheme;
	private String createdTime;
	private String updatedTime;

	public UserSetting() {

	}

	public UserSetting(String userSettingId, long userId, String sceneObjectId, String fontStyle, String colorScheme,
			String createdTime, String updatedTime) {
		this.userSettingId = userSettingId;
		this.userId = userId;
		this.sceneObjectId = sceneObjectId;
		this.fontStyle = fontStyle;
		this.colorScheme = colorScheme;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
	}

	public String getUserSettingId() {
		return userSettingId;
	}

	public void setUserSettingId(String userSettingId) {
		this.userSettingId = userSettingId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getSceneObjectId() {
		return sceneObjectId;
	}

	public void setSceneObjectId(String sceneObjectId) {
		this.sceneObjectId = sceneObjectId;
	}

	public String getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(String fontStyle) {
		this.fontStyle = fontStyle;
	}

	public String getColorScheme() {
		return colorScheme;
	}

	public void setColorScheme(String colorScheme) {
		this.colorScheme = colorScheme;
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
		return "UserSetting [userSettingId=" + userSettingId + ", userId=" + userId + ", sceneObjectId=" + sceneObjectId
				+ ", fontStyle=" + fontStyle + ", colorScheme=" + colorScheme + ", createdTime=" + createdTime
				+ ", updatedTime=" + updatedTime + "]";
	}
}
