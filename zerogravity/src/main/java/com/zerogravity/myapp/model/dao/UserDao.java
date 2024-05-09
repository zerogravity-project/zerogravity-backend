package com.zerogravity.myapp.model.dao;

import com.zerogravity.myapp.model.dto.User;
import com.zerogravity.myapp.model.dto.UserInfo;

public interface UserDao {
	public abstract User selectUserByUserId(long userId);

	public abstract UserInfo selectUserInfoByUserId(long userId);

	public abstract int insertUser(User user);

	public abstract int deleteUser(long userId);
}
