package com.zerogravity.myapp.model.dao;

import com.zerogravity.myapp.model.dto.User;

/**
 * DAO interface for user database operations
 * Handles user authentication and profile management
 */
public interface UserDao {

	/**
	 * Select user by user ID
	 * @param userId User ID
	 * @return User object or null if not found
	 */
	public abstract User selectUserByUserId(long userId);

	/**
	 * Select user by OAuth provider ID and provider name
	 * @param providerId OAuth provider ID
	 * @param provider Provider name (e.g., "kakao")
	 * @return User object or null if not found
	 */
	public abstract User selectUserByProviderIdAndProvider(String providerId, String provider);

	/**
	 * Insert a new user
	 * @param user User object to insert
	 * @return Number of rows affected
	 */
	public abstract int insertUser(User user);

	/**
	 * Update user information
	 * @param user User object with updated information
	 * @return Number of rows affected
	 */
	public abstract int updateUser(User user);

	/**
	 * Delete a user by user ID
	 * @param userId User ID to delete
	 * @return Number of rows affected
	 */
	public abstract int deleteUser(long userId);

}
