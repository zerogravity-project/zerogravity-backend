package com.zerogravity.myapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerogravity.myapp.model.dto.UserSetting;
import com.zerogravity.myapp.model.service.UserSettingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api-zerogravity/users")
@Tag(name = "User Setting Management", description = "사용자 설정 관리 API")
public class UserSettingRestController {

	private final UserSettingService userSettingService;

	@Autowired
	public UserSettingRestController(UserSettingService userSettingService) {
		this.userSettingService = userSettingService;
	}

	@GetMapping("/{userId}/settings")
	@Operation(summary = "사용자 설정 조회", description = "특정 사용자의 설정을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "사용자 설정을 성공적으로 찾았습니다."),
        @ApiResponse(responseCode = "404", description = "해당 사용자의 설정 정보를 찾을 수 없습니다.")
    })
	public ResponseEntity<?> getUserSettingByUserId(@PathVariable long userId) {
		UserSetting userSetting = userSettingService.getUserSettingByUserId(userId);
		if (userSetting != null) {
			return new ResponseEntity<UserSetting>(userSetting, HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/{userId}/settings")
	@Operation(summary = "사용자 설정 생성", description = "새로운 사용자 설정을 생성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "사용자 설정이 성공적으로 생성되었습니다."),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터로 인해 설정 생성에 실패하였습니다.")
    })
	public ResponseEntity<Void> createUserSetting(@PathVariable long userId, @Parameter(description = "사용자 설정") @RequestBody UserSetting userSetting) {
		userSetting.setUserId(userId);

		if (isValidUserSetting(userSetting)) {
			boolean isCreated = userSettingService.createUserSetting(userSetting);
			if (isCreated) {
				return new ResponseEntity<Void>(HttpStatus.CREATED);
			}
		}
		return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);

	}

	@PutMapping("/{userId}/settings")
	@Operation(summary = "사용자 설정 업데이트", description = "기존 사용자 설정을 업데이트합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "사용자 설정이 성공적으로 업데이트되었습니다."),
        @ApiResponse(responseCode = "404", description = "업데이트할 사용자 설정을 찾을 수 없습니다."),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터로 인해 설정 업데이트에 실패하였습니다.")
    })
	public ResponseEntity<Void> updateUserSetting(@PathVariable long userId, @Parameter(description = "사용자 설정") @RequestBody UserSetting userSetting) {
		userSetting.setUserId(userId);

		if (isValidUserSetting(userSetting)) {
			boolean isUpdated = userSettingService.modifyUserSetting(userSetting);
			if (isUpdated) {
				return new ResponseEntity<Void>(HttpStatus.OK);
			} else {
				if (userSettingService.getUserSettingByUserId(userId) == null) {
					return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
				}
			}
		}
		return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);

	}

	private boolean isValidUserSetting(UserSetting userSetting) {
		return userSetting != null && userSetting.getSceneObjectId() != null && userSetting.getFontStyle() != null
				&& userSetting.getColorScheme() != null;
	}
}
