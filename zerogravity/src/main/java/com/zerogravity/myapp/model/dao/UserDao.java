package com.zerogravity.myapp.model.dao;

import com.zerogravity.myapp.model.dto.User;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;

/**
 * DAO interface for user database operations
 * Handles user authentication and profile management
 */
public interface UserDao {

	/**
	 * Select user by user ID (excluding soft-deleted users)
	 * @param userId User ID
	 * @return User object or null if not found
	 */
	User selectUserByUserId(@Param("userId") Long userId);

	/**
	 * Select user by OAuth provider ID and provider name (excluding soft-deleted users)
	 * @param providerId OAuth provider ID
	 * @param provider Provider name (e.g., "kakao", "google")
	 * @return User object or null if not found
	 */
	User selectUserByProviderIdAndProvider(@Param("providerId") String providerId, @Param("provider") String provider);

	/**
	 * Insert a new user
	 * @param user User object to insert
	 * @return Number of rows affected
	 */
	int insertUser(User user);

	/**
	 * Update user information
	 * @param user User object with updated information
	 * @return Number of rows affected
	 */
	int updateUser(User user);

	/**
	 * Soft delete a user by user ID
	 * @param userId User ID to delete
	 * @param deletedAt Deletion timestamp
	 * @return Number of rows affected
	 */
	int softDeleteUser(@Param("userId") Long userId, @Param("deletedAt") Timestamp deletedAt);

	/**
	 * Hard delete a user by user ID (for admin use only)
	 * @param userId User ID to delete
	 * @return Number of rows affected
	 */
	int deleteUser(@Param("userId") Long userId);

}
