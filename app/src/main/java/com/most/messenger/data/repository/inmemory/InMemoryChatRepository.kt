package com.most.messenger.data.repository.inmemory

import com.most.messenger.data.model.Chat
import com.most.messenger.data.model.ChatType
import com.most.messenger.data.model.Message
import com.most.messenger.data.repository.AuthRepository
import com.most.messenger.data.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class InMemoryChatRepository(
    private val authRepository: AuthRepository,
    private val store: InMemoryMostStore
) : ChatRepository {

    override fun observeChatsForCurrentUser(): Flow<List<Chat>> {
        val uid = authRepository.currentUserId ?: "demoUser"
        return store.chats.map { chats ->
            chats.filter { uid in it.memberIds }.sortedByDescending { it.lastMessageAt ?: 0L }
        }
    }

    override fun observeMessages(chatId: String): Flow<List<Message>> {
        return store.messagesByChat.map { it[chatId].orEmpty().sortedBy { message -> message.createdAt } }
    }

    override suspend fun sendTextMessage(chatId: String, text: String): Result<Unit> = runCatching {
        val uid = authRepository.currentUserId ?: "demoUser"
        require(text.isNotBlank()) { "Message can't be blank" }
        store.addMessage(chatId = chatId, senderId = uid, text = text.trim())
    }

    override suspend fun createDirectChatIfNeeded(otherUserId: String): Result<String> = runCatching {
        val uid = authRepository.currentUserId ?: "demoUser"
        val existing = store.chats.value.firstOrNull { chat ->
            chat.type == ChatType.DIRECT && chat.memberIds.containsAll(listOf(uid, otherUserId))
        }
        existing?.id ?: UUID.randomUUID().toString()
    }

    override suspend fun createGroupChat(title: String, memberIds: List<String>): Result<String> = runCatching {
        // TODO(next): persist and expose dynamic group creation for production data source.
        UUID.randomUUID().toString()
    }
}
