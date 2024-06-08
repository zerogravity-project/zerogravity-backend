package com.zerogravity.myapp.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
	@Operation(summary = "사용자 정보 조회", description = "특정 사용자의 정보를 조회합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "사용자 정보 찾음"),
			@ApiResponse(responseCode = "204", description = "로그인 안됨"),
			@ApiResponse(responseCode = "401", description = "인증 실패"),
			@ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음") })
	public ResponseEntity<?> getProfile(@CookieValue(value = "token", required = false) String token) {
		if (token == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		if (jwtUtil.isExpired(token)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		Long userId = jwtUtil.getUserId(token);
		User user = userService.getUserByUserId(userId);
		if (user != null) {
			Map<String, Object> userProfile = Map.of("nickname", user.getNickname(), "profileImage",
					user.getProfileImage(), "thumbnailImage", user.getThumbnailImage());
			return new ResponseEntity<>(userProfile, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/logout")
	@Operation(summary = "로그아웃", description = "사용자를 로그아웃 시키고 JWT 쿠키를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "로그아웃 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
	public ResponseEntity<Void> logout(@CookieValue(value = "token", required = false) String token, HttpServletResponse response) {
		if (token != null) {
			Cookie jwtCookie = new Cookie("token", null);
			jwtCookie.setHttpOnly(true);
			jwtCookie.setPath("/");
			jwtCookie.setMaxAge(0); // 쿠키를 즉시 만료
			response.addCookie(jwtCookie);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@DeleteMapping("/me")
	@Operation(summary = "사용자 삭제", description = "현재 사용자를 삭제합니다.")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "사용자 삭제 성공"),
	    @ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음") })
	public ResponseEntity<Void> removeCurrentUser(@CookieValue(value = "token", required = false) String token, HttpServletResponse response) {
	    if (token == null || jwtUtil.isExpired(token)) {
	        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	    }

	    Long userId = jwtUtil.getUserId(token);
	    boolean isDeleted = userService.removeUser(userId);
	    if (isDeleted) {
	        // JWT 쿠키 삭제
	        Cookie jwtCookie = new Cookie("token", null);
	        jwtCookie.setHttpOnly(true);
	        jwtCookie.setPath("/");
	        jwtCookie.setMaxAge(0); // 쿠키를 즉시 만료
	        response.addCookie(jwtCookie);

	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}

	@GetMapping("/{userId}/info")
	@Operation(summary = "사용자 추가 정보 조회", description = "특정 사용자의 추가 정보를 조회합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "사용자 추가 정보 찾음"),
			@ApiResponse(responseCode = "404", description = "사용자 추가 정보를 찾을 수 없음") })
	public ResponseEntity<?> getUserInfoByUserId(@PathVariable long userId) {
		UserInfo userInfo = userService.getUserInfoByUserId(userId);
		if (userInfo != null) {
			return new ResponseEntity<UserInfo>(userInfo, HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping
	@Operation(summary = "새 사용자 생성", description = "새로운 사용자와 사용자 정보를 생성합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "사용자 생성 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청으로 인한 사용자 생성 실패") })
	public ResponseEntity<Void> createUser(@Parameter(description = "사용자 정보") @RequestBody User user) {
		if (isValidUser(user)) {
			boolean isCreated = userService.createUser(user);
			if (isCreated) {
				return new ResponseEntity<>(HttpStatus.CREATED);
			}
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/{userId}")
	@Operation(summary = "사용자 삭제", description = "특정 사용자를 삭제합니다.")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "사용자 삭제 성공"),
			@ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음") })
	public ResponseEntity<Void> removeUser(@PathVariable long userId) {
		boolean isDeleted = userService.removeUser(userId);
		if (isDeleted) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}

	private boolean isValidUser(User user) {
		return user != null;
	}

	private boolean isValidUserInfo(UserInfo userInfo) {
		return userInfo != null;
	}

}
