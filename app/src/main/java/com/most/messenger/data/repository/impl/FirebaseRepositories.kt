package com.most.messenger.data.repository.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.most.messenger.data.model.Chat
import com.most.messenger.data.model.Message
import com.most.messenger.data.model.Quest
import com.most.messenger.data.model.QuestInput
import com.most.messenger.data.model.UserProfile
import com.most.messenger.data.repository.AuthRepository
import com.most.messenger.data.repository.ChatRepository
import com.most.messenger.data.repository.QuestRepository
import com.most.messenger.data.repository.UserRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : AuthRepository {
    override suspend fun signIn(email: String, password: String): Result<Unit> = runCatching {
        auth.signInWithEmailAndPassword(email, password).await()
        Unit
    }

    override suspend fun signUp(email: String, password: String): Result<Unit> = runCatching {
        auth.createUserWithEmailAndPassword(email, password).await()
        Unit
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override fun currentUserId(): String? = auth.currentUser?.uid
}

class FirebaseUserRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : UserRepository {
    override suspend fun createOrUpdateProfile(profile: UserProfile): Result<Unit> = runCatching {
        val uid = auth.currentUser?.uid ?: error("No authenticated user")
        firestore.collection("users").document(uid).set(profile.copy(id = uid)).await()
        Unit
    }

    override suspend fun getCurrentUserProfile(): Result<UserProfile?> = runCatching {
        val uid = auth.currentUser?.uid ?: return@runCatching null
        firestore.collection("users").document(uid).get().await().toObject(UserProfile::class.java)
    }

    override fun observeUserProfile(userId: String): Flow<UserProfile?> = callbackFlow {
        val listener = firestore.collection("users").document(userId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(value?.toObject(UserProfile::class.java))
            }
        awaitClose { listener.remove() }
    }
}

class FirebaseChatRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ChatRepository {
    override fun observeChatsForCurrentUser(userId: String): Flow<List<Chat>> = callbackFlow {
        val listener = firestore.collection("chats")
            .whereArrayContains("memberIds", userId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val chats = value?.documents?.mapNotNull { it.toObject(Chat::class.java)?.copy(id = it.id) }.orEmpty()
                trySend(chats)
            }
        awaitClose { listener.remove() }
    }

    override fun observeMessages(chatId: String): Flow<List<Message>> = callbackFlow {
        val listener = firestore.collection("chats").document(chatId).collection("messages")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val messages = value?.documents?.mapNotNull { it.toObject(Message::class.java)?.copy(id = it.id) }.orEmpty()
                trySend(messages)
            }
        awaitClose { listener.remove() }
    }

    override suspend fun sendTextMessage(chatId: String, text: String): Result<Unit> = runCatching {
        val sender = auth.currentUser?.uid ?: error("No authenticated user")
        val messages = firestore.collection("chats").document(chatId).collection("messages")
        val message = Message(chatId = chatId, senderId = sender, text = text)
        messages.add(message).await()
        firestore.collection("chats").document(chatId)
            .update(mapOf("lastMessageText" to text, "lastMessageAt" to System.currentTimeMillis()))
            .await()
        Unit
    }

    override suspend fun createDirectChatIfNeeded(otherUserId: String): Result<String> = runCatching {
        // TODO Step 3: query for existing direct chats and reuse chat when available.
        val currentUser = auth.currentUser?.uid ?: error("No authenticated user")
        val chat = Chat(
            type = com.most.messenger.data.model.ChatType.DIRECT,
            memberIds = listOf(currentUser, otherUserId),
            createdBy = currentUser,
            createdAt = System.currentTimeMillis()
        )
        val ref = firestore.collection("chats").add(chat).await()
        ref.id
    }

    override suspend fun createGroupChat(title: String, memberIds: List<String>): Result<String> = runCatching {
        val currentUser = auth.currentUser?.uid ?: error("No authenticated user")
        val chat = Chat(
            type = com.most.messenger.data.model.ChatType.GROUP,
            title = title,
            memberIds = memberIds.distinct(),
            createdBy = currentUser,
            createdAt = System.currentTimeMillis()
        )
        firestore.collection("chats").add(chat).await().id
    }
}

class FirebaseQuestRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : QuestRepository {
    override suspend fun createDirectQuest(chatId: String, recipientId: String, input: QuestInput): Result<String> =
        runCatching {
            // TODO Step 4: create quest + quest message atomically in a batch write.
            val quest = Quest(title = input.title)
            firestore.collection("quests").add(quest).await().id
        }

    override suspend fun createOpenGroupQuest(chatId: String, input: QuestInput): Result<String> = runCatching {
        // TODO Step 4: add source and assignment metadata when quest creation flow is implemented.
        firestore.collection("quests").add(Quest(title = input.title)).await().id
    }

    override suspend fun createPersonalQuest(input: QuestInput): Result<String> = runCatching {
        val userId = auth.currentUser?.uid ?: error("No authenticated user")
        val quest = Quest(title = input.title, createdBy = userId)
        firestore.collection("quests").add(quest).await().id
    }

    override fun observeDashboardQuests(userId: String): Flow<List<Quest>> = callbackFlow {
        // TODO Step 5: combine direct/group/personal quest filters for dashboard tabs.
        val listener = firestore.collection("quests")
            .whereEqualTo("createdBy", userId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val quests = value?.documents?.mapNotNull { it.toObject(Quest::class.java)?.copy(id = it.id) }.orEmpty()
                trySend(quests)
            }
        awaitClose { listener.remove() }
    }

    override fun observeGroupQuests(chatId: String): Flow<List<Quest>> = callbackFlow {
        val listener = firestore.collection("quests")
            .whereEqualTo("sourceChatId", chatId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val quests = value?.documents?.mapNotNull { it.toObject(Quest::class.java)?.copy(id = it.id) }.orEmpty()
                trySend(quests)
            }
        awaitClose { listener.remove() }
    }

    override suspend fun acceptQuest(questId: String): Result<Unit> = updateStatusStub(questId)
    override suspend fun declineQuest(questId: String): Result<Unit> = updateStatusStub(questId)
    override suspend fun saveQuestForLater(questId: String): Result<Unit> = updateStatusStub(questId)
    override suspend fun claimQuest(questId: String): Result<Unit> = updateStatusStub(questId)
    override suspend fun releaseQuest(questId: String): Result<Unit> = updateStatusStub(questId)
    override suspend fun completeQuest(questId: String): Result<Unit> = updateStatusStub(questId)

    private suspend fun updateStatusStub(questId: String): Result<Unit> = runCatching {
        // TODO Step 4/5: apply business rules and transition quest status per action.
        firestore.collection("quests").document(questId).update("updatedAt", System.currentTimeMillis()).await()
        Unit
    }
}
