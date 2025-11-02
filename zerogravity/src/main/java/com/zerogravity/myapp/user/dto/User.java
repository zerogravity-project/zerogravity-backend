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

	// User Consent Fields
	@Schema(description = "Terms of service agreement", example = "false")
	private Boolean termsAgreed;

	@Schema(description = "Terms agreement timestamp", example = "2021-12-01T12:00:00Z")
	private Timestamp termsAgreedAt;

	@Schema(description = "Privacy policy agreement", example = "false")
	private Boolean privacyAgreed;

	@Schema(description = "Privacy policy agreement timestamp", example = "2021-12-01T12:00:00Z")
	private Timestamp privacyAgreedAt;

	@Schema(description = "Sensitive data (emotion) processing consent", example = "false")
	private Boolean sensitiveDataConsent;

	@Schema(description = "Sensitive data consent timestamp", example = "2021-12-01T12:00:00Z")
	private Timestamp sensitiveDataConsentAt;

	@Schema(description = "AI analysis feature consent", example = "false")
	private Boolean aiAnalysisConsent;

	@Schema(description = "AI analysis consent timestamp", example = "2021-12-01T12:00:00Z")
	private Timestamp aiAnalysisConsentAt;

	@Schema(description = "Consent document version", example = "v1.0")
	private String consentVersion;

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

	public Boolean getTermsAgreed() {
		return termsAgreed;
	}

	public void setTermsAgreed(Boolean termsAgreed) {
		this.termsAgreed = termsAgreed;
	}

	public Timestamp getTermsAgreedAt() {
		return termsAgreedAt;
	}

	public void setTermsAgreedAt(Timestamp termsAgreedAt) {
		this.termsAgreedAt = termsAgreedAt;
	}

	public Boolean getPrivacyAgreed() {
		return privacyAgreed;
	}

	public void setPrivacyAgreed(Boolean privacyAgreed) {
		this.privacyAgreed = privacyAgreed;
	}

	public Timestamp getPrivacyAgreedAt() {
		return privacyAgreedAt;
	}

	public void setPrivacyAgreedAt(Timestamp privacyAgreedAt) {
		this.privacyAgreedAt = privacyAgreedAt;
	}

	public Boolean getSensitiveDataConsent() {
		return sensitiveDataConsent;
	}

	public void setSensitiveDataConsent(Boolean sensitiveDataConsent) {
		this.sensitiveDataConsent = sensitiveDataConsent;
	}

	public Timestamp getSensitiveDataConsentAt() {
		return sensitiveDataConsentAt;
	}

	public void setSensitiveDataConsentAt(Timestamp sensitiveDataConsentAt) {
		this.sensitiveDataConsentAt = sensitiveDataConsentAt;
	}

	public Boolean getAiAnalysisConsent() {
		return aiAnalysisConsent;
	}

	public void setAiAnalysisConsent(Boolean aiAnalysisConsent) {
		this.aiAnalysisConsent = aiAnalysisConsent;
	}

	public Timestamp getAiAnalysisConsentAt() {
		return aiAnalysisConsentAt;
	}

	public void setAiAnalysisConsentAt(Timestamp aiAnalysisConsentAt) {
		this.aiAnalysisConsentAt = aiAnalysisConsentAt;
	}

	public String getConsentVersion() {
		return consentVersion;
	}

	public void setConsentVersion(String consentVersion) {
		this.consentVersion = consentVersion;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", providerId=" + providerId + ", provider=" + provider + ", email="
				+ email + ", name=" + name + ", image=" + image + ", isDeleted=" + isDeleted + ", deletedAt="
				+ deletedAt + ", createdTime=" + createdTime + ", updatedTime=" + updatedTime + "]";
	}
}
