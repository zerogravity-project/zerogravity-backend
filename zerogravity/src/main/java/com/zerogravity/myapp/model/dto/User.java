package com.zerogravity.myapp.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 정보")
public class User {
	@Schema(description = "사용자 ID", example = "1")
	private long userId;
	
	@Schema(description = "사용자 이메일", example = "user@example.com")
	private String email;
	
	@Schema(description = "사용자 이름", example = "표다영")
	private String name;
	
	@Schema(description = "사용자 별명", example = "데이지")
	private String profileNickname;
	
	@Schema(description = "프로필 이미지 URL", example = "https://example.com/profiles/daisy.jpg")
	private String profileImgUrl;
	
	@Schema(description = "생성 시간", example = "2021-10-01T12:00:00Z")
	private String createdTime;
	
	@Schema(description = "업데이트 시간", example = "2021-12-01T12:00:00Z")
	private String updatedTime;
	
	public User() {
		
	}
	
	public User(long userId, String email, String name, String profileNickname, String profileImgUrl,
			String createdTime, String updatedTime) {
		this.userId = userId;
		this.email = email;
		this.name = name;
		this.profileNickname = profileNickname;
		this.profileImgUrl = profileImgUrl;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProfileNickname() {
		return profileNickname;
	}

	public void setProfileNickname(String profileNickname) {
		this.profileNickname = profileNickname;
	}

	public String getProfileImgUrl() {
		return profileImgUrl;
	}

	public void setProfileImgUrl(String profileImgUrl) {
		this.profileImgUrl = profileImgUrl;
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
		return "User [userId=" + userId + ", email=" + email + ", name=" + name + ", profileNickname=" + profileNickname
				+ ", profileImgUrl=" + profileImgUrl + ", createdTime=" + createdTime + ", updatedTime=" + updatedTime
				+ "]";
	}
}
