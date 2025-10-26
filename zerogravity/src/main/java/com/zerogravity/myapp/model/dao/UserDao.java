package com.zerogravity.myapp.model.dao;

import com.zerogravity.myapp.model.dto.User;
import com.zerogravity.myapp.model.dto.UserInfo;

public interface UserDao {
	// 사용자 정보 조회
	public abstract User selectUserByUserId(long userId);

	// OAuth 제공자 ID와 제공자명으로 사용자 조회
	public abstract User selectUserByProviderIdAndProvider(String providerId, String provider);

	// 사용자 추가 정보 조회
	public abstract UserInfo selectUserInfoByUserId(long userId);

	// 사용자 정보 삽입
	public abstract int insertUser(User user);

	// 사용자 정보 삽입
	public abstract int updateUser(User user);

	// 사용자 추가 정보 삽입
	public abstract int insertUserInfo(UserInfo userInfo);

	// 사용자 정보 삭제
	public abstract int deleteUser(long userId);
}
