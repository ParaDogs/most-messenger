package com.most.messenger.data.repository

import com.most.messenger.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun createOrUpdateProfile(profile: UserProfile): Result<Unit>
    suspend fun getCurrentUserProfile(): Result<UserProfile?>
    fun observeUserProfile(userId: String): Flow<UserProfile?>
}
