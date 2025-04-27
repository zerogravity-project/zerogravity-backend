package com.zerogravity.myapp.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 정보")
public class User {
	@Schema(description = "사용자 ID", example = "1")
	private long userId;
	
	@Schema(description = "사용자 별명", example = "데이지")
	private String nickname;
	
	@Schema(description = "프로필 이미지 URL", example = "https://example.com/profiles/daisy.jpg")
	private String profileImage;
	
	@Schema(description = "프로필 이미지 URL", example = "https://example.com/profiles/daisy.jpg")
	private String thumbnailImage;
	
	@Schema(description = "생성 시간", example = "2021-10-01T12:00:00Z")
	private String createdTime;
	
	@Schema(description = "업데이트 시간", example = "2021-12-01T12:00:00Z")
	private String updatedTime;
	
	public User() {
		
	}

	public User(long userId, String nickname, String profileImage, String thumbnailImage) {
		super();
		this.userId = userId;
		this.nickname = nickname;
		this.profileImage = profileImage;
		this.thumbnailImage = thumbnailImage;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public String getThumbnailImage() {
		return thumbnailImage;
	}

	public void setThumbnailImage(String thumbnailImage) {
		this.thumbnailImage = thumbnailImage;
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
		return "User [userId=" + userId + ", nickname=" + nickname + ", profileImage=" + profileImage
				+ ", thumbnailImage=" + thumbnailImage + ", createdTime=" + createdTime + ", updatedTime=" + updatedTime
				+ "]";
	}
}
