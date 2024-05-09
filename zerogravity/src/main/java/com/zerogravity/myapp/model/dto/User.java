package com.zerogravity.myapp.model.dto;

public class User {
	private long userId;
	private String email;
	private String name;
	private String profileNickname;
	private String profileImgUrl;
	private String createdTime;
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
