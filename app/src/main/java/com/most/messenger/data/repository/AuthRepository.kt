package com.most.messenger.data.repository

interface AuthRepository {
    val currentUserId: String?

    suspend fun signIn(email: String, password: String): Result<String>
    suspend fun signUp(email: String, password: String): Result<String>
    fun signOut()
}
