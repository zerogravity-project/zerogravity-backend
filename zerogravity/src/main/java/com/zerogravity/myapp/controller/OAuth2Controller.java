package com.zerogravity.myapp.controller;

import java.net.URI;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.zerogravity.myapp.model.dto.User;
import com.zerogravity.myapp.model.service.UserService;
import com.zerogravity.myapp.security.JWTUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/login/oauth2/code")
public class OAuth2Controller {
	@Value("${kakao.client.id}")
    private String kakaoClientId;

    @Value("${kakao.client.secret}")
    private String kakaoClientSecret;

    @Value("${kakao.redirect.uri}")
    private String kakaoRedirectUri;

	private final UserService userService;
	private final JWTUtil jwtUtil;

	@Autowired
	public OAuth2Controller(UserService userService, JWTUtil jwtUtil) {
		this.userService = userService;
		this.jwtUtil = jwtUtil;
	}

	@GetMapping("/kakao")
	public ResponseEntity<?> getAccessToken(@RequestParam("code") String code, HttpServletResponse response) {

		// 1. 토큰 받기
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);
        params.add("client_secret", kakaoClientSecret);

		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, httpHeaders);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Map> tokenResponse;
		try {
			tokenResponse = restTemplate.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, requestEntity,
					Map.class);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		Map<String, Object> responseBody = tokenResponse.getBody();
		if (responseBody == null || !responseBody.containsKey("access_token")) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		String accessToken = (String) responseBody.get("access_token");

		// 2. 사용자 정보 요청
		HttpHeaders userInfoHeaders = new HttpHeaders();
		userInfoHeaders.setBearerAuth(accessToken);
		HttpEntity<String> userInfoEntity = new HttpEntity<>(userInfoHeaders);
		ResponseEntity<Map> userInfoResponse;
		try {
			userInfoResponse = restTemplate.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.GET,
					userInfoEntity, Map.class);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		Map<String, Object> userInfo = userInfoResponse.getBody();
		if (userInfo == null || !userInfo.containsKey("id")) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		long kakaoId = ((Number) userInfo.get("id")).longValue();
		String nickname = (String) ((Map) userInfo.get("properties")).get("nickname");
		String profileImageUrl = (String) ((Map) userInfo.get("properties")).get("profile_image");
		String thumbnailImageUrl = (String) ((Map) userInfo.get("properties")).get("thumbnail_image");

		User user = userService.getUserByUserId(kakaoId);

		if (user == null) {
			// 사용자가 존재하지 않으면 새로 삽입
			user = new User(kakaoId, nickname, profileImageUrl, thumbnailImageUrl);
			userService.createUser(user);
		} else {
			// 존재하는 사용자면 정보 업데이트
			user.setNickname(nickname);
			user.setProfileImage(profileImageUrl);
			user.setThumbnailImage(thumbnailImageUrl);
			userService.modifyUser(user);
		}

		// 3. JWT 생성
		String jwt = jwtUtil.createJwt(kakaoId, 3600000L); // 1시간 만료

		// 4. Cookie에 전달
		Cookie jwtCookie = new Cookie("token", jwt);
		jwtCookie.setHttpOnly(true);
		jwtCookie.setPath("/");
		jwtCookie.setMaxAge(3600); // 1시간

		response.addCookie(jwtCookie);

		// 사용자 정보와 JWT 반환
		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/")).build();

	}
}
