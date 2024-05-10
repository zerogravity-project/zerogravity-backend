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

@RestController
@RequestMapping("/api-zerogravity/users")
public class UserSettingRestController {

	private final UserSettingService userSettingService;

	@Autowired
	public UserSettingRestController(UserSettingService userSettingService) {
		this.userSettingService = userSettingService;
	}

	@GetMapping("/{userId}/settings")
	public ResponseEntity<?> getUserSettingByUserId(@PathVariable long userId) {
		UserSetting userSetting = userSettingService.getUserSettingByUserId(userId);
		if (userSetting != null) {
			return new ResponseEntity<UserSetting>(userSetting, HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/{userId}/settings")
	public ResponseEntity<Void> createUserSetting(@PathVariable long userId, @RequestBody UserSetting userSetting) {
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
	public ResponseEntity<Void> updateUserSetting(@PathVariable long userId, @RequestBody UserSetting userSetting) {
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
