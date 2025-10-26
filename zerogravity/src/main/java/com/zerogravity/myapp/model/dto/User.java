package com.zerogravity.myapp.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 정보")
public class User {
	@Schema(description = "사용자 ID (내부, Snowflake ID)", example = "1234567890123456789")
	private Long userId;

	@Schema(description = "OAuth 제공자 ID", example = "123456789")
	private String providerId;

	@Schema(description = "OAuth 제공자", example = "google")
	private String provider;

	@Schema(description = "사용자 이메일", example = "user@example.com")
	private String email;

	@Schema(description = "사용자 이름", example = "홍길동")
	private String name;

	@Schema(description = "프로필 이미지 URL", example = "https://example.com/profiles/daisy.jpg")
	private String image;

	@Schema(description = "생성 시간", example = "2021-10-01T12:00:00Z")
	private String createdTime;

	@Schema(description = "업데이트 시간", example = "2021-12-01T12:00:00Z")
	private String updatedTime;

	public User() {
	}

	public User(String providerId, String provider, String email, String name, String image) {
		super();
		this.providerId = providerId;
		this.provider = provider;
		this.email = email;
		this.name = name;
		this.image = image;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
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
		return "User [userId=" + userId + ", providerId=" + providerId + ", provider=" + provider + ", email="
				+ email + ", name=" + name + ", image=" + image + ", createdTime=" + createdTime + ", updatedTime="
				+ updatedTime + "]";
	}
}
