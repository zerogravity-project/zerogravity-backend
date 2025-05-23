package com.zerogravity.myapp.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zerogravity.myapp.model.dao.UserDao;
import com.zerogravity.myapp.model.dto.User;
import com.zerogravity.myapp.model.dto.UserInfo;

@Service
public class UserServiceImpl implements UserService {
	private final UserDao userDao;
	
	@Autowired
	public UserServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public User getUserByUserId(long userId) {
		return userDao.selectUserByUserId(userId);
	}

	@Override
	public UserInfo getUserInfoByUserId(long userId) {
		return userDao.selectUserInfoByUserId(userId);
	}

	@Override
	@Transactional
	public boolean createUser(User user) {
		int userResult = userDao.insertUser(user);
		if(userResult == 1) {
			return true;
		}
		return false;
	}
	
	@Override
	@Transactional
	public boolean modifyUser(User user) {
		int userResult = userDao.updateUser(user);
		if(userResult == 1) {
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public boolean removeUser(long userId) {
		int result = userDao.deleteUser(userId);
		return result == 1;
	}

}
