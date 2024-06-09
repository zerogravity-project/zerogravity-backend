package com.zerogravity.myapp.model.service;

import com.zerogravity.myapp.model.dto.User;
import com.zerogravity.myapp.model.dto.UserInfo;

public interface UserService {
	// 사용자 정보 조회
	public abstract User getUserByUserId(long userId);

	// 사용자 추가 정보 조회
	public abstract UserInfo getUserInfoByUserId(long userId);

	// 사용자 정보 삽입
	public abstract boolean createUser(User user);

	// 사용자 정보 업데이트
	public abstract boolean modifyUser(User user);

	// 사용자 정보 삭제
	public abstract boolean removeUser(long userId);

}
