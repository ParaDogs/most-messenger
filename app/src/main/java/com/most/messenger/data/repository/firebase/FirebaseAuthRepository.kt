package com.most.messenger.data.repository.firebase

import com.google.firebase.auth.FirebaseAuth
import com.most.messenger.data.repository.AuthRepository
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : AuthRepository {

    override val currentUserId: String?
        get() = auth.currentUser?.uid

    override suspend fun signIn(email: String, password: String): Result<String> = runCatching {
        auth.signInWithEmailAndPassword(email, password).await().user?.uid
            ?: error("Failed to sign in")
    }

    override suspend fun signUp(email: String, password: String): Result<String> = runCatching {
        auth.createUserWithEmailAndPassword(email, password).await().user?.uid
            ?: error("Failed to sign up")
    }

    override fun signOut() {
        auth.signOut()
    }
}
