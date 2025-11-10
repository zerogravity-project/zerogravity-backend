package com.zerogravity.myapp.user.controller;

import java.util.Map;

import com.zerogravity.myapp.common.security.AuthUserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerogravity.myapp.auth.service.RefreshTokenService;
import com.zerogravity.myapp.user.dto.User;
import com.zerogravity.myapp.user.dto.ConsentUpdateRequest;
import com.zerogravity.myapp.user.service.UserService;
import com.zerogravity.myapp.common.dto.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import com.zerogravity.myapp.common.util.TimezoneUtil;
import java.time.Instant;
import java.time.ZoneId;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "User Management API")
public class UserRestController {

	private final UserService userService;
	private final RefreshTokenService refreshTokenService;

	@Autowired
	public UserRestController(UserService userService, RefreshTokenService refreshTokenService) {
		this.userService = userService;
		this.refreshTokenService = refreshTokenService;
	}

	@GetMapping("/me")
	@Operation(summary = "Get User Information", description = "Retrieve information of the currently logged-in user.")
	public ResponseEntity<?> getProfile(
		@AuthUserId Long userId,
		@RequestHeader(value = "X-Timezone", defaultValue = "UTC") String clientTimezone
	) {
		try {
			User user = userService.getUserByUserId(userId);
			if (user != null) {
				ZoneId timezone = ZoneId.of(clientTimezone);

				Map<String, Object> userProfile = new java.util.LinkedHashMap<>();
				userProfile.put("name", user.getName());
				userProfile.put("image", user.getImage());
				userProfile.put("email", user.getEmail());
				userProfile.put("provider", user.getProvider());
				userProfile.put("consents", buildConsentsMap(user, timezone));

				return new ResponseEntity<>(userProfile, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to get user profile: " + e.getMessage());
		}
	}

	@PostMapping("/logout")
	@Operation(
			summary = "Logout",
			description = "Logout by revoking all refresh tokens and deleting JWT cookie. " +
					"This invalidates all active sessions for the user."
	)
	public ResponseEntity<ApiResponse<Void>> logout(
			@AuthUserId Long userId,
			HttpServletResponse response
	) {
		try {
			// Revoke all refresh tokens for this user
			refreshTokenService.revokeAllUserTokens(userId);

			// Delete JWT cookie
			Cookie jwtCookie = new Cookie("token", null);
			jwtCookie.setHttpOnly(true);
			jwtCookie.setPath("/");
			jwtCookie.setMaxAge(0);
			response.addCookie(jwtCookie);

			ApiResponse<Void> apiResponse = new ApiResponse<>(
					null,
					Instant.now().toString()
			);

			return ResponseEntity.ok(apiResponse);
		} catch (Exception e) {
			System.err.println("[UserRestController] Error during logout: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/me")
	@Operation(summary = "Delete User", description = "Delete the currently logged-in user.")
	public ResponseEntity<Void> removeUser(@AuthUserId Long userId, HttpServletResponse response) {
		boolean isDeleted = userService.removeUser(userId);
		if (isDeleted) {
			Cookie jwtCookie = new Cookie("token", null);
			jwtCookie.setHttpOnly(true);
			jwtCookie.setPath("/");
			jwtCookie.setMaxAge(0);
			response.addCookie(jwtCookie);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/consent")
	@Operation(summary = "Update User Consent", description = "Update user consent preferences for terms, privacy, and sensitive data processing.")
	public ResponseEntity<?> updateConsent(
		@AuthUserId Long userId,
		@RequestHeader(value = "X-Timezone", defaultValue = "UTC") String clientTimezone,
		@RequestBody ConsentUpdateRequest request
	) {
		try {
			// Validate required consents
			if (request.getTermsAgreed() == null || !request.getTermsAgreed()) {
				throw new IllegalArgumentException("Terms agreement is required");
			}
			if (request.getPrivacyAgreed() == null || !request.getPrivacyAgreed()) {
				throw new IllegalArgumentException("Privacy agreement is required");
			}
			if (request.getSensitiveDataConsent() == null || !request.getSensitiveDataConsent()) {
				throw new IllegalArgumentException("Sensitive data consent is required");
			}

			ZoneId timezone = ZoneId.of(clientTimezone);

			// Update consent
			boolean updated = userService.updateConsent(userId, request);
			if (updated) {
				// Fetch updated user to return current consent state
				User updatedUser = userService.getUserByUserId(userId);
				Map<String, Object> consentsData = Map.of(
					"consents", buildConsentsMap(updatedUser, timezone)
				);

				ApiResponse<Map<String, Object>> response = new ApiResponse<>(
					consentsData,
					TimezoneUtil.formatToUserTimezone(Instant.now(), timezone)
				);
				return ResponseEntity.ok(response);
			} else {
				throw new IllegalArgumentException("User not found or cannot be updated");
			}
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid request: " + e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException("Failed to update consent: " + e.getMessage());
		}
	}

	/**
	 * Helper method to build consent information map from User object
	 * @param user User object with consent fields
	 * @param timezone User's timezone for timestamp formatting
	 * @return Map with all consent fields and timestamps
	 */
	private Map<String, Object> buildConsentsMap(User user, ZoneId timezone) {
		Map<String, Object> consents = new java.util.LinkedHashMap<>();

		// Terms agreement
		consents.put("termsAgreed", user.getTermsAgreed() != null ? user.getTermsAgreed() : false);
		if (user.getTermsAgreedAt() != null) {
			consents.put("termsAgreedAt", TimezoneUtil.formatToUserTimezone(user.getTermsAgreedAt().toInstant(), timezone));
		} else {
			consents.put("termsAgreedAt", null);
		}

		// Privacy agreement
		consents.put("privacyAgreed", user.getPrivacyAgreed() != null ? user.getPrivacyAgreed() : false);
		if (user.getPrivacyAgreedAt() != null) {
			consents.put("privacyAgreedAt", TimezoneUtil.formatToUserTimezone(user.getPrivacyAgreedAt().toInstant(), timezone));
		} else {
			consents.put("privacyAgreedAt", null);
		}

		// Sensitive data consent
		consents.put("sensitiveDataConsent", user.getSensitiveDataConsent() != null ? user.getSensitiveDataConsent() : false);
		if (user.getSensitiveDataConsentAt() != null) {
			consents.put("sensitiveDataConsentAt", TimezoneUtil.formatToUserTimezone(user.getSensitiveDataConsentAt().toInstant(), timezone));
		} else {
			consents.put("sensitiveDataConsentAt", null);
		}

		// AI analysis consent
		consents.put("aiAnalysisConsent", user.getAiAnalysisConsent() != null ? user.getAiAnalysisConsent() : false);
		if (user.getAiAnalysisConsentAt() != null) {
			consents.put("aiAnalysisConsentAt", TimezoneUtil.formatToUserTimezone(user.getAiAnalysisConsentAt().toInstant(), timezone));
		} else {
			consents.put("aiAnalysisConsentAt", null);
		}

		// Consent version
		consents.put("consentVersion", user.getConsentVersion() != null ? user.getConsentVersion() : "v1.0");

		return consents;
	}

	// POST endpoint removed - user creation only via /auth/verify
}
