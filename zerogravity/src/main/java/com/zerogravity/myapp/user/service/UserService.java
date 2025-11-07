package com.zerogravity.myapp.user.service;

import com.zerogravity.myapp.user.dto.User;
import com.zerogravity.myapp.user.dto.ConsentUpdateRequest;

/**
 * Service interface for user management
 * Handles user authentication and profile management
 */
public interface UserService {

	/**
	 * Get user by user ID
	 * @param userId User ID
	 * @return User object or null if not found
	 */
	public abstract User getUserByUserId(Long userId);

	/**
	 * Get user by OAuth provider ID and provider name
	 * @param providerId OAuth provider ID
	 * @param provider Provider name (e.g., "kakao")
	 * @return User object or null if not found
	 */
	public abstract User getUserByProviderIdAndProvider(String providerId, String provider);

	/**
	 * Create a new user
	 * @param user User object to create
	 * @return true if creation successful, false otherwise
	 */
	public abstract boolean createUser(User user);

	/**
	 * Update user information
	 * @param user User object with updated information
	 * @return true if update successful, false otherwise
	 */
	public abstract boolean modifyUser(User user);

	/**
	 * Delete a user by user ID
	 * @param userId User ID to delete
	 * @return true if deletion successful, false otherwise
	 */
	public abstract boolean removeUser(Long userId);

	/**
	 * Update user consent information
	 * @param userId User ID
	 * @param request Consent update request
	 * @return true if update successful, false otherwise
	 */
	public abstract boolean updateConsent(Long userId, ConsentUpdateRequest request);

}
