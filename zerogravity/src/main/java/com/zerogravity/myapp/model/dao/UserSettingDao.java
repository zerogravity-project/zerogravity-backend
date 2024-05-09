package com.zerogravity.myapp.model.dao;

import com.zerogravity.myapp.model.dto.UserSetting;

public interface UserSettingDao {
	public abstract UserSetting selectUserSettingByUserId(long userId);

	public abstract int insertUserSetting(UserSetting userSetting);

	public abstract int updateUserSetting(UserSetting userSetting);
}
