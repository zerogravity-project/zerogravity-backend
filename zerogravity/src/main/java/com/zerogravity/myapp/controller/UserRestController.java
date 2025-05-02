package com.zerogravity.myapp.controller;

import java.util.Map;

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
import com.zerogravity.myapp.model.dto.UserInfo;
import com.zerogravity.myapp.model.service.UserService;
import com.zerogravity.myapp.security.JWTUtil;

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
	private final JWTUtil jwtUtil;

	@Autowired
	public UserRestController(UserService userService, JWTUtil jwtUtil) {
		this.userService = userService;
		this.jwtUtil = jwtUtil;
	}

	@GetMapping("/me")
	@Operation(summary = "사용자 정보 조회", description = "현재 로그인된 사용자의 정보를 조회합니다.")
	public ResponseEntity<?> getProfile(@CookieValue(value = "token", required = false) String token) {
		if (token == null || jwtUtil.isExpired(token)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		Long userId = jwtUtil.getUserId(token);
		User user = userService.getUserByUserId(userId);
		if (user != null) {
			Map<String, Object> userProfile = Map.of(
					"nickname", user.getNickname(),
					"profileImage", user.getProfileImage(),
					"thumbnailImage", user.getThumbnailImage()
			);
			return new ResponseEntity<>(userProfile, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/info")
	@Operation(summary = "사용자 추가 정보 조회", description = "현재 로그인된 사용자의 추가 정보를 조회합니다.")
	public ResponseEntity<?> getUserInfoByToken(@CookieValue(value = "token", required = false) String token) {
		if (token == null || jwtUtil.isExpired(token)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		Long userId = jwtUtil.getUserId(token);
		UserInfo userInfo = userService.getUserInfoByUserId(userId);
		if (userInfo != null) {
			return new ResponseEntity<>(userInfo, HttpStatus.OK);
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
	public ResponseEntity<Void> removeUser(@CookieValue(value = "token", required = false) String token, HttpServletResponse response) {
		if (token == null || jwtUtil.isExpired(token)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		Long userId = jwtUtil.getUserId(token);
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

	@PostMapping
	@Operation(summary = "새 사용자 생성", description = "새로운 사용자를 생성합니다.")
	public ResponseEntity<Void> createUser(@Parameter(description = "사용자 정보") @RequestBody User user) {
		if (isValidUser(user)) {
			boolean isCreated = userService.createUser(user);
			if (isCreated) {
				return new ResponseEntity<>(HttpStatus.CREATED);
			}
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	private boolean isValidUser(User user) {
		return user != null;
	}

	private boolean isValidUserInfo(UserInfo userInfo) {
		return userInfo != null;
	}
}
