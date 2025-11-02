package com.zerogravity.myapp.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User Consent Update Request")
public class ConsentUpdateRequest {

	@Schema(description = "Terms of service agreement", example = "true")
	private Boolean termsAgreed;

	@Schema(description = "Privacy policy agreement", example = "true")
	private Boolean privacyAgreed;

	@Schema(description = "Sensitive data (emotion) processing consent", example = "true")
	private Boolean sensitiveDataConsent;

	@Schema(description = "AI analysis feature consent (optional)", example = "false")
	private Boolean aiAnalysisConsent;

	public ConsentUpdateRequest() {
	}

	public ConsentUpdateRequest(Boolean termsAgreed, Boolean privacyAgreed, Boolean sensitiveDataConsent, Boolean aiAnalysisConsent) {
		this.termsAgreed = termsAgreed;
		this.privacyAgreed = privacyAgreed;
		this.sensitiveDataConsent = sensitiveDataConsent;
		this.aiAnalysisConsent = aiAnalysisConsent;
	}

	public Boolean getTermsAgreed() {
		return termsAgreed;
	}

	public void setTermsAgreed(Boolean termsAgreed) {
		this.termsAgreed = termsAgreed;
	}

	public Boolean getPrivacyAgreed() {
		return privacyAgreed;
	}

	public void setPrivacyAgreed(Boolean privacyAgreed) {
		this.privacyAgreed = privacyAgreed;
	}

	public Boolean getSensitiveDataConsent() {
		return sensitiveDataConsent;
	}

	public void setSensitiveDataConsent(Boolean sensitiveDataConsent) {
		this.sensitiveDataConsent = sensitiveDataConsent;
	}

	public Boolean getAiAnalysisConsent() {
		return aiAnalysisConsent;
	}

	public void setAiAnalysisConsent(Boolean aiAnalysisConsent) {
		this.aiAnalysisConsent = aiAnalysisConsent;
	}

	@Override
	public String toString() {
		return "ConsentUpdateRequest [termsAgreed=" + termsAgreed + ", privacyAgreed=" + privacyAgreed
				+ ", sensitiveDataConsent=" + sensitiveDataConsent + ", aiAnalysisConsent=" + aiAnalysisConsent + "]";
	}
}
