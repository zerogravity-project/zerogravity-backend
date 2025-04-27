package com.zerogravity.myapp.model.dao;

import com.zerogravity.myapp.model.dto.UserSetting;

public interface UserSettingDao {
	// 사용자 설정 정보 조회
	public abstract UserSetting selectUserSettingByUserId(long userId);

	// 사용자 설정 정보 삽입
	public abstract int insertUserSetting(UserSetting userSetting);

	// 사용자 설정 정보 업데이트
	public abstract int updateUserSetting(UserSetting userSetting);
}
