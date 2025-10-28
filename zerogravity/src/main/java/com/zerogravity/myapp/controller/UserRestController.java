package com.zerogravity.myapp.controller;

import java.util.Map;

import com.zerogravity.myapp.security.AuthUserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerogravity.myapp.model.dto.User;
import com.zerogravity.myapp.model.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api-zerogravity/users")
@Tag(name = "User Management", description = "사용자 관리 API")
public class UserRestController {

	private final UserService userService;

	@Autowired
	public UserRestController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/me")
	@Operation(summary = "사용자 정보 조회", description = "현재 로그인된 사용자의 정보를 조회합니다.")
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
	@Operation(summary = "로그아웃", description = "JWT 쿠키를 삭제하여 로그아웃합니다.")
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
	@Operation(summary = "사용자 삭제", description = "현재 로그인된 사용자를 삭제합니다.")
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

	// POST endpoint removed - user creation only via /auth/verify
}
