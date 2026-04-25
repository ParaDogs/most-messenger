package com.most.messenger.data.repository

import com.most.messenger.data.model.Chat
import com.most.messenger.data.model.Message
import com.most.messenger.data.model.Quest
import com.most.messenger.data.model.QuestInput
import com.most.messenger.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signUp(email: String, password: String): Result<Unit>
    suspend fun signOut()
    fun currentUserId(): String?
}

interface UserRepository {
    suspend fun createOrUpdateProfile(profile: UserProfile): Result<Unit>
    suspend fun getCurrentUserProfile(): Result<UserProfile?>
    fun observeUserProfile(userId: String): Flow<UserProfile?>
}

interface ChatRepository {
    fun observeChatsForCurrentUser(userId: String): Flow<List<Chat>>
    fun observeMessages(chatId: String): Flow<List<Message>>
    suspend fun sendTextMessage(chatId: String, text: String): Result<Unit>
    suspend fun createDirectChatIfNeeded(otherUserId: String): Result<String>
    suspend fun createGroupChat(title: String, memberIds: List<String>): Result<String>
}

interface QuestRepository {
    suspend fun createDirectQuest(chatId: String, recipientId: String, input: QuestInput): Result<String>
    suspend fun createOpenGroupQuest(chatId: String, input: QuestInput): Result<String>
    suspend fun createPersonalQuest(input: QuestInput): Result<String>
    fun observeDashboardQuests(userId: String): Flow<List<Quest>>
    fun observeGroupQuests(chatId: String): Flow<List<Quest>>
    suspend fun acceptQuest(questId: String): Result<Unit>
    suspend fun declineQuest(questId: String): Result<Unit>
    suspend fun saveQuestForLater(questId: String): Result<Unit>
    suspend fun claimQuest(questId: String): Result<Unit>
    suspend fun releaseQuest(questId: String): Result<Unit>
    suspend fun completeQuest(questId: String): Result<Unit>
}
