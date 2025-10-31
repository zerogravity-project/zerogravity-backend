package com.zerogravity.myapp.user.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Timestamp;

@Schema(description = "User Information")
public class User {
	@Schema(description = "User ID (internal, Snowflake ID)", example = "1234567890123456789")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long userId;

	@Schema(description = "OAuth Provider ID", example = "123456789")
	private String providerId;

	@Schema(description = "OAuth Provider", example = "google")
	private String provider;

	@Schema(description = "User Email", example = "user@example.com")
	private String email;

	@Schema(description = "User Name", example = "John Doe")
	private String name;

	@Schema(description = "Profile Image URL", example = "https://example.com/profiles/daisy.jpg")
	private String image;

	@Schema(description = "Deleted status", example = "false")
	private Boolean isDeleted;

	@Schema(description = "Deleted time", example = "2021-12-01T12:00:00Z")
	private Timestamp deletedAt;

	@Schema(description = "Created time", example = "2021-10-01T12:00:00Z")
	private Timestamp createdTime;

	@Schema(description = "Updated time", example = "2021-12-01T12:00:00Z")
	private Timestamp updatedTime;

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

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Timestamp getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Timestamp deletedAt) {
		this.deletedAt = deletedAt;
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
		return "User [userId=" + userId + ", providerId=" + providerId + ", provider=" + provider + ", email="
				+ email + ", name=" + name + ", image=" + image + ", isDeleted=" + isDeleted + ", deletedAt="
				+ deletedAt + ", createdTime=" + createdTime + ", updatedTime=" + updatedTime + "]";
	}
}
