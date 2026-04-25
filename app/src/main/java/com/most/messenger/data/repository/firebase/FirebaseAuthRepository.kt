package com.most.messenger.data.repository.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.most.messenger.data.repository.AuthRepository
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : AuthRepository {

    override val currentUserId: String?
        get() = auth.currentUser?.uid

    override suspend fun signIn(email: String, password: String): Result<String> = runCatching {
        val user = auth.signInWithEmailAndPassword(email, password).await().user
            ?: error("Failed to sign in")
        Log.i("FirebaseAuthRepository", "signIn success uid=${user.uid} email=${user.email}")
        user.uid
    }

    override suspend fun signUp(email: String, password: String): Result<String> = runCatching {
        val user = auth.createUserWithEmailAndPassword(email, password).await().user
            ?: error("Failed to sign up")
        Log.i("FirebaseAuthRepository", "signUp success uid=${user.uid} email=${user.email}")
        user.uid
    }

    override fun signOut() {
        auth.signOut()
    }
}
