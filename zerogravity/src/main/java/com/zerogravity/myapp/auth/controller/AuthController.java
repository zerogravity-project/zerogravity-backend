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
import com.zerogravity.myapp.user.dto.User;
import com.zerogravity.myapp.user.service.UserService;
import com.zerogravity.myapp.common.security.JWTUtil;
import com.zerogravity.myapp.common.security.SnowflakeIdService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Authentication Controller for OAuth verification and JWT generation
 * Handles NextAuth integration: receives OAuth user info and returns backend JWT
 */
@RestController
@RequestMapping("/api-zerogravity/auth")
@Tag(name = "Auth", description = "Authentication API")
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
	@Operation(summary = "OAuth User Verification and JWT Issuance", description = "Endpoint called after OAuth login from NextAuth. Verifies user information and issues backend JWT.")
	public ResponseEntity<AuthResponse> verifyAndCreateUser(@RequestBody User oauthUser, HttpServletResponse response) {
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
				long newUserId = snowflakeIdService.generateId();
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

			// 5. Set JWT as HttpOnly cookie
			Cookie cookie = new Cookie("token", jwtToken);
			cookie.setHttpOnly(true);        // JS cannot access
			cookie.setSecure(true);          // HTTPS only (production)
			cookie.setPath("/");             // All paths
			cookie.setMaxAge(3600);          // 1 hour (seconds)
			cookie.setAttribute("SameSite", "Lax");  // CSRF protection
			
			response.addCookie(cookie);

			// Response body is only success message
			AuthResponse authResponse = new AuthResponse(true, "Authentication successful");
			return ResponseEntity.ok(authResponse);

		} catch (Exception e) {
			System.err.println("[AuthController] Error during OAuth verification: " + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
