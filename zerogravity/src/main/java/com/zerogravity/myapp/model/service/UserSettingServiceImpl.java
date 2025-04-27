package com.zerogravity.myapp.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zerogravity.myapp.model.dao.UserSettingDao;
import com.zerogravity.myapp.model.dto.UserSetting;

@Service
public class UserSettingServiceImpl implements UserSettingService {
	
	private final UserSettingDao userSettingDao;
	
	@Autowired
	public UserSettingServiceImpl(UserSettingDao userSettingDao) {
		this.userSettingDao = userSettingDao;
	}

	@Override
	public UserSetting getUserSettingByUserId(long userId) {
		return userSettingDao.selectUserSettingByUserId(userId);
	}

	@Override
	@Transactional
	public boolean createUserSetting(UserSetting userSetting) {
		int result = userSettingDao.insertUserSetting(userSetting);
		return result == 1;
	}

	@Override
	@Transactional
	public boolean modifyUserSetting(UserSetting userSetting) {
		int result = userSettingDao.updateUserSetting(userSetting);
		return result == 1;
	}
}
