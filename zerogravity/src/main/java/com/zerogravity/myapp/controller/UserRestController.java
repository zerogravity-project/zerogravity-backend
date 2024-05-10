package com.zerogravity.myapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("/api-zerogravity/users")
public class UserRestController {

	private final UserService userService;

	@Autowired
	public UserRestController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/{userId}")
	public ResponseEntity<?> getUserByUserId(@PathVariable long userId) {
		User user = userService.getUserByUserId(userId);
		if (user != null) {
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/{userId}/info")
	public ResponseEntity<?> getUserInfoByUserId(@PathVariable long userId) {
		UserInfo userInfo = userService.getUserInfoByUserId(userId);
		if (userInfo != null) {
			return new ResponseEntity<UserInfo>(userInfo, HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping
	public ResponseEntity<Void> createUser(@RequestBody User user, @RequestBody UserInfo userInfo) {
		if (isValidUser(user) && isValidUserInfo(userInfo)) {
			boolean isCreated = userService.createUser(user, userInfo);
			if (isCreated) {
				return new ResponseEntity<>(HttpStatus.CREATED);
			}
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<Void> removeUser(@PathVariable long userId) {
		boolean isDeleted = userService.removeUser(userId);
		if (isDeleted) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}

	private boolean isValidUser(User user) {
		return user != null && user.getEmail() != null && user.getName() != null;
	}

	private boolean isValidUserInfo(UserInfo userInfo) {
		return userInfo != null;
	}

}
