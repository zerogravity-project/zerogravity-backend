package com.zerogravity.myapp.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 설정 정보")
public class UserSetting {
	@Schema(description = "사용자 설정 ID", example = "abc123")
	private String userSettingId;
	
	@Schema(description = "사용자 ID", example = "1")
	private long userId;
	
	@Schema(description = "3D 오브젝트 ID", example = "abc123")
	private String sceneObjectId;
	
	@Schema(description = "폰트 스타일", example = "Arial")
	private String fontStyle;
	
	@Schema(description = "색깔", example = "Red")
	private String colorScheme;
	
	@Schema(description = "생성 시간", example = "2021-10-01T12:00:00Z")
	private String createdTime;
	
	@Schema(description = "업데이트 시간", example = "2021-12-01T12:00:00Z")
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
