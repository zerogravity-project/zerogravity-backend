package com.zerogravity.myapp.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zerogravity.myapp.user.dao.UserDao;
import com.zerogravity.myapp.user.dto.User;
import com.zerogravity.myapp.user.dto.ConsentUpdateRequest;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class UserServiceImpl implements UserService {
	private final UserDao userDao;

	@Autowired
	public UserServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	@Transactional(readOnly = true)
	public User getUserByUserId(Long userId) {
		return userDao.selectUserByUserId(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public User getUserByProviderIdAndProvider(String providerId, String provider) {
		return userDao.selectUserByProviderIdAndProvider(providerId, provider);
	}

	@Override
	@Transactional
	public boolean createUser(User user) {
		int userResult = userDao.insertUser(user);
		return userResult == 1;
	}

	@Override
	@Transactional
	public boolean modifyUser(User user) {
		int userResult = userDao.updateUser(user);
		return userResult == 1;
	}

	@Override
	@Transactional
	public boolean removeUser(Long userId) {
		// Soft delete
		Timestamp deletedAt = Timestamp.from(Instant.now());
		int result = userDao.softDeleteUser(userId, deletedAt);
		return result == 1;
	}

	@Override
	@Transactional
	public boolean updateConsent(Long userId, ConsentUpdateRequest request) {
		int result = userDao.updateConsent(userId, request);
		return result == 1;
	}

}
