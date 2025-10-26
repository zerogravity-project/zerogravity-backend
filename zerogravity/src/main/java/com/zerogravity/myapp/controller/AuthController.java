package com.zerogravity.myapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerogravity.myapp.model.dto.AuthResponse;
import com.zerogravity.myapp.model.dto.User;
import com.zerogravity.myapp.model.service.UserService;
import com.zerogravity.myapp.security.JWTUtil;
import com.zerogravity.myapp.security.SnowflakeIdService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Authentication Controller for OAuth verification and JWT generation
 * Handles NextAuth integration: receives OAuth user info and returns backend JWT
 */
@RestController
@RequestMapping("/api-zerogravity/auth")
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {
	private final UserService userService;
	private final JWTUtil jwtUtil;
	private final SnowflakeIdService snowflakeIdService;

	@Autowired
	public AuthController(UserService userService, JWTUtil jwtUtil, SnowflakeIdService snowflakeIdService) {
		this.userService = userService;
		this.jwtUtil = jwtUtil;
		this.snowflakeIdService = snowflakeIdService;
	}

	/**
	 * Verify OAuth user and generate backend JWT
	 * Called by frontend (NextAuth) after OAuth login
	 *
	 * @param oauthUser OAuth user information from the frontend
	 * @return AuthResponse with JWT token
	 */
	@PostMapping("/verify")
	@Operation(summary = "OAuth 사용자 검증 및 JWT 발급", description = "NextAuth에서 OAuth 로그인 후 호출하는 엔드포인트. 사용자 정보를 검증하고 백엔드 JWT를 발급합니다.")
	public ResponseEntity<AuthResponse> verifyAndCreateUser(@RequestBody User oauthUser) {
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

			// 2. Create a new user if it doesn't exist
			if (user == null) {
				// Generate Snowflake ID (unique, non-sequential, sortable)
				long newUserId = snowflakeIdService.generateUserId();
				oauthUser.setUserId(newUserId);

				// Create user
				boolean created = userService.createUser(oauthUser);
				if (!created) {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
				}
				user = oauthUser;
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

			// 4. Generate JWT token (1-hour expiration)
			String jwtToken = jwtUtil.createJwt(user.getUserId(), 3600000L);

			// 5. Return response
			AuthResponse response = new AuthResponse(jwtToken);
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			System.err.println("[AuthController] Error during OAuth verification: " + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
