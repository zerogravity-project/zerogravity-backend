package com.zerogravity.myapp.model.service;

import com.zerogravity.myapp.model.dto.UserSetting;

public interface UserSettingService {
	// 사용자 설정 정보 조회
	public abstract UserSetting getUserSettingByUserId(long userId);
	
	// 사용자 설정 정보 삽입
	public abstract boolean createUserSetting(UserSetting userSetting);
	
	// 사용자 설정 정보 업데이트
	public abstract boolean modifyUserSetting(UserSetting userSetting);
}
