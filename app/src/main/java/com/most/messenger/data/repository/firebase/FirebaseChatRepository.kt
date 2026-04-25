package com.most.messenger.data.repository.firebase

import com.most.messenger.data.model.Chat
import com.most.messenger.data.model.Message
import com.most.messenger.data.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FirebaseChatRepository : ChatRepository {
    override fun observeChatsForCurrentUser(): Flow<List<Chat>> {
        // TODO(step-3): Observe chats collection where current user is in memberIds.
        return flowOf(emptyList())
    }

    override fun observeMessages(chatId: String): Flow<List<Message>> {
        // TODO(step-3): Observe chats/{chatId}/messages in createdAt order.
        return flowOf(emptyList())
    }

    override suspend fun sendTextMessage(chatId: String, text: String): Result<Unit> {
        // TODO(step-3): Persist text message and update chat's lastMessage fields.
        return Result.success(Unit)
    }

    override suspend fun createDirectChatIfNeeded(otherUserId: String): Result<String> {
        // TODO(step-3): Find-or-create a direct chat between current user and otherUserId.
        return Result.success("")
    }

    override suspend fun createGroupChat(title: String, memberIds: List<String>): Result<String> {
        // TODO(step-3): Create a new group chat.
        return Result.success("")
    }
}
