package com.zerogravity.myapp.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerogravity.myapp.auth.dto.AuthResponse;
import com.zerogravity.myapp.auth.dto.RefreshRequest;
import com.zerogravity.myapp.auth.dto.RefreshResponse;
import com.zerogravity.myapp.auth.service.RefreshTokenService;
import com.zerogravity.myapp.common.dto.ApiResponse;
import com.zerogravity.myapp.user.dto.User;
import com.zerogravity.myapp.user.service.UserService;
import com.zerogravity.myapp.common.security.JWTUtil;
import com.zerogravity.myapp.common.security.SnowflakeIdService;
import com.zerogravity.myapp.common.util.TimezoneUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Map;

/**
 * Authentication Controller for OAuth verification and JWT generation
 * Handles NextAuth integration: receives OAuth user info and returns backend JWT
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Authentication API")
public class AuthController {
	private final UserService userService;
	private final JWTUtil jwtUtil;
	private final SnowflakeIdService snowflakeIdService;
	private final RefreshTokenService refreshTokenService;

	@Autowired
	public AuthController(
			UserService userService,
			JWTUtil jwtUtil,
			SnowflakeIdService snowflakeIdService,
			RefreshTokenService refreshTokenService
	) {
		this.userService = userService;
		this.jwtUtil = jwtUtil;
		this.snowflakeIdService = snowflakeIdService;
		this.refreshTokenService = refreshTokenService;
	}

	/**
	 * Verify OAuth user and generate backend JWT
	 * Called by frontend (NextAuth) after OAuth login
	 *
	 * @param oauthUser OAuth user information from the frontend
	 * @return AuthResponse with JWT token
	 */
	@PostMapping("/verify")
	@Operation(summary = "OAuth User Verification and JWT Issuance", description = "Endpoint called after OAuth login from NextAuth. Verifies user information and issues backend JWT.")
	public ResponseEntity<ApiResponse<AuthResponse>> verifyAndCreateUser(@RequestBody User oauthUser, HttpServletResponse response) {
		try {
			// Validate input
			if (oauthUser == null || oauthUser.getProviderId() == null || oauthUser.getProvider() == null) {
				return ResponseEntity.badRequest().build();
			}

			// 1. Check if a user exists by providerId + provider
			User user = userService.getUserByProviderIdAndProvider(
				oauthUser.getProviderId(),
				oauthUser.getProvider()
			);

			// Track if user is new
			boolean isNewUser = false;

			// 2. Create a new user if it doesn't exist
			if (user == null) {
				// Generate Snowflake ID (unique, non-sequential, sortable)
				long newUserId = snowflakeIdService.generateId();
				oauthUser.setUserId(newUserId);

				// Create user with default consent values (all false)
				boolean created = userService.createUser(oauthUser);
				if (!created) {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
				}
				user = oauthUser;
				isNewUser = true;
			} else {
				// 3. Update user info if the user already exists
				user.setEmail(oauthUser.getEmail());
				user.setName(oauthUser.getName());
				user.setImage(oauthUser.getImage());
				boolean modified = userService.modifyUser(user);
				if (!modified) {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
				}
			}

			// 4. Generate JWT token (15-minute expiration)
			String jwtToken = jwtUtil.createJwt(user.getUserId(), 900000L);

			// 5. Generate refresh token (30-day expiration)
			String refreshToken = refreshTokenService.createRefreshToken(user.getUserId());

			// 6. Set JWT as HttpOnly cookie
			Cookie cookie = new Cookie("token", jwtToken);
			cookie.setHttpOnly(true);        // JS cannot access
			cookie.setSecure(true);          // HTTPS only (production)
			cookie.setPath("/");             // All paths
			cookie.setMaxAge(900);           // 15 minutes (seconds)
			cookie.setAttribute("SameSite", "Lax");  // CSRF protection

			response.addCookie(cookie);

			// Build consents map with UTC timezone (frontend will receive and handle timezone conversion)
			Map<String, Object> consents = buildConsentsMap(user);

			// Response with isNewUser flag and both tokens for frontend
			AuthResponse authResponse = new AuthResponse(
					true,
					"Authentication successful",
					isNewUser,
					consents,
					jwtToken,
					refreshToken
			);
			ApiResponse<AuthResponse> apiResponse = new ApiResponse<>(authResponse, Instant.now().toString());
			return ResponseEntity.ok(apiResponse);

		} catch (Exception e) {
			System.err.println("[AuthController] Error during OAuth verification: " + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Helper method to build consent information map from User object
	 * @param user User object with consent fields
	 * @return Map with all consent fields and timestamps (formatted as UTC)
	 */
	private Map<String, Object> buildConsentsMap(User user) {
		Map<String, Object> consents = new java.util.LinkedHashMap<>();
		ZoneId utcTimezone = ZoneId.of("UTC");

		// Terms agreement
		consents.put("termsAgreed", user.getTermsAgreed() != null ? user.getTermsAgreed() : false);
		if (user.getTermsAgreedAt() != null) {
			consents.put("termsAgreedAt", TimezoneUtil.formatToUserTimezone(user.getTermsAgreedAt().toInstant(), utcTimezone));
		} else {
			consents.put("termsAgreedAt", null);
		}

		// Privacy agreement
		consents.put("privacyAgreed", user.getPrivacyAgreed() != null ? user.getPrivacyAgreed() : false);
		if (user.getPrivacyAgreedAt() != null) {
			consents.put("privacyAgreedAt", TimezoneUtil.formatToUserTimezone(user.getPrivacyAgreedAt().toInstant(), utcTimezone));
		} else {
			consents.put("privacyAgreedAt", null);
		}

		// Sensitive data consent
		consents.put("sensitiveDataConsent", user.getSensitiveDataConsent() != null ? user.getSensitiveDataConsent() : false);
		if (user.getSensitiveDataConsentAt() != null) {
			consents.put("sensitiveDataConsentAt", TimezoneUtil.formatToUserTimezone(user.getSensitiveDataConsentAt().toInstant(), utcTimezone));
		} else {
			consents.put("sensitiveDataConsentAt", null);
		}

		// AI analysis consent
		consents.put("aiAnalysisConsent", user.getAiAnalysisConsent() != null ? user.getAiAnalysisConsent() : false);
		if (user.getAiAnalysisConsentAt() != null) {
			consents.put("aiAnalysisConsentAt", TimezoneUtil.formatToUserTimezone(user.getAiAnalysisConsentAt().toInstant(), utcTimezone));
		} else {
			consents.put("aiAnalysisConsentAt", null);
		}

		// Consent version
		consents.put("consentVersion", user.getConsentVersion() != null ? user.getConsentVersion() : "v1.0");

		return consents;
	}

	/**
	 * Refresh access token using refresh token
	 * Implements token rotation: old refresh token is revoked and new tokens are issued
	 *
	 * @param request refresh token request
	 * @return RefreshResponse with new access token and refresh token
	 */
	@PostMapping("/refresh")
	@Operation(
			summary = "Refresh Access Token",
			description = "Exchange refresh token for new access token and refresh token. " +
					"Implements token rotation for security: old refresh token is immediately revoked."
	)
	public ResponseEntity<ApiResponse<RefreshResponse>> refreshToken(@RequestBody RefreshRequest request) {
		try {
			// Validate request
			if (request == null || request.getRefreshToken() == null || request.getRefreshToken().isBlank()) {
				return ResponseEntity.badRequest().build();
			}

			// Validate and rotate refresh token
			RefreshTokenService.TokenPair tokenPair = refreshTokenService.validateAndRotateRefreshToken(
					request.getRefreshToken()
			);

			// Create response with new tokens
			RefreshResponse refreshResponse = new RefreshResponse(
					tokenPair.accessToken(),
					tokenPair.refreshToken()
			);

			ApiResponse<RefreshResponse> apiResponse = new ApiResponse<>(
					refreshResponse,
					Instant.now().toString()
			);

			return ResponseEntity.ok(apiResponse);

		} catch (Exception e) {
			System.err.println("[AuthController] Error during token refresh: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
}
