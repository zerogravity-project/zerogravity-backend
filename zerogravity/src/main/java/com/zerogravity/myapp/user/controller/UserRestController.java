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
@RequestMapping("/api-zerogravity/users")
@Tag(name = "User Management", description = "User Management API")
public class UserRestController {

	private final UserService userService;

	@Autowired
	public UserRestController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/me")
	@Operation(summary = "Get User Information", description = "Retrieve information of the currently logged-in user.")
	public ResponseEntity<?> getProfile(@AuthUserId Long userId) {
		User user = userService.getUserByUserId(userId);
		if (user != null) {
			Map<String, Object> userProfile = Map.of(
					"name", user.getName(),
					"image", user.getImage(),
					"email", user.getEmail(),
					"provider", user.getProvider()
			);
			return new ResponseEntity<>(userProfile, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/logout")
	@Operation(summary = "Logout", description = "Logout by deleting JWT cookie.")
	public ResponseEntity<Void> logout(@CookieValue(value = "token", required = false) String token, HttpServletResponse response) {
		if (token != null) {
			Cookie jwtCookie = new Cookie("token", null);
			jwtCookie.setHttpOnly(true);
			jwtCookie.setPath("/");
			jwtCookie.setMaxAge(0);
			response.addCookie(jwtCookie);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
				ApiResponse<Void> response = new ApiResponse<>(
					null,
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

	// POST endpoint removed - user creation only via /auth/verify
}
