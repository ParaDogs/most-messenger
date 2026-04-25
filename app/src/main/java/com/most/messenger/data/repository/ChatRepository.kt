package com.most.messenger.data.repository

import com.most.messenger.data.model.Chat
import com.most.messenger.data.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun observeChatsForCurrentUser(): Flow<List<Chat>>
    fun observeMessages(chatId: String): Flow<List<Message>>
    suspend fun sendTextMessage(chatId: String, text: String): Result<Unit>
    suspend fun createDirectChatIfNeeded(otherUserId: String): Result<String>
    suspend fun createGroupChat(title: String, memberIds: List<String>): Result<String>
}
