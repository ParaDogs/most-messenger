package com.most.messenger.data.repository.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.most.messenger.data.model.UserProfile
import com.most.messenger.data.repository.AuthRepository
import com.most.messenger.data.repository.UserRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseUserRepository(
    private val authRepository: AuthRepository,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : UserRepository {

    override suspend fun createOrUpdateProfile(profile: UserProfile): Result<Unit> = runCatching {
        firestore.collection("users").document(profile.id).set(profile).await()
    }

    override suspend fun getCurrentUserProfile(): Result<UserProfile?> = runCatching {
        val uid = authRepository.currentUserId ?: return@runCatching null
        firestore.collection("users").document(uid).get().await().toObject(UserProfile::class.java)
    }

    override fun observeUserProfile(userId: String): Flow<UserProfile?> = callbackFlow {
        val registration: ListenerRegistration = firestore.collection("users").document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.toObject(UserProfile::class.java))
            }

        awaitClose { registration.remove() }
    }
}
