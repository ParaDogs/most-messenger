package com.most.messenger.data.repository.inmemory

import com.most.messenger.data.model.Chat
import com.most.messenger.data.model.ChatType
import com.most.messenger.data.model.Message
import com.most.messenger.data.model.MessageType
import com.most.messenger.data.model.Quest
import com.most.messenger.data.model.QuestAssignmentType
import com.most.messenger.data.model.QuestDifficulty
import com.most.messenger.data.model.QuestSourceType
import com.most.messenger.data.model.QuestStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class InMemoryMostStore {
    private val _chats = MutableStateFlow(seedChats())
    private val _messagesByChat = MutableStateFlow(seedMessages())
    private val _quests = MutableStateFlow(seedQuests())

    val chats: StateFlow<List<Chat>> = _chats.asStateFlow()
    val messagesByChat: StateFlow<Map<String, List<Message>>> = _messagesByChat.asStateFlow()
    val quests: StateFlow<List<Quest>> = _quests.asStateFlow()

    fun addMessage(chatId: String, senderId: String, text: String) {
        val message = Message(
            id = UUID.randomUUID().toString(),
            chatId = chatId,
            senderId = senderId,
            type = MessageType.TEXT,
            text = text,
            createdAt = System.currentTimeMillis()
        )
        _messagesByChat.update { map ->
            val list = map[chatId].orEmpty() + message
            map + (chatId to list)
        }
        _chats.update { chats ->
            chats.map { chat ->
                if (chat.id == chatId) {
                    chat.copy(lastMessageText = text, lastMessageAt = message.createdAt)
                } else chat
            }
        }
    }

    fun addQuestAndLinkedMessage(quest: Quest) {
        _quests.update { it + quest }
        quest.sourceChatId?.let { chatId ->
            val message = Message(
                id = UUID.randomUUID().toString(),
                chatId = chatId,
                senderId = quest.createdBy,
                type = MessageType.QUEST,
                text = "Quest: ${quest.title}",
                questId = quest.id,
                createdAt = quest.createdAt
            )
            _messagesByChat.update { map ->
                val list = map[chatId].orEmpty() + message
                map + (chatId to list)
            }
            _chats.update { chats ->
                chats.map { chat ->
                    if (chat.id == chatId) {
                        chat.copy(lastMessageText = "🧭 ${quest.title}", lastMessageAt = quest.createdAt)
                    } else chat
                }
            }
        }
    }

    fun updateQuest(questId: String, updater: (Quest) -> Quest) {
        _quests.update { quests ->
            quests.map { quest -> if (quest.id == questId) updater(quest) else quest }
        }
    }

    companion object {
        private fun seedChats(): List<Chat> {
            val now = System.currentTimeMillis()
            return listOf(
                Chat(
                    id = "direct_anna",
                    type = ChatType.DIRECT,
                    title = "Anna",
                    memberIds = listOf("demoUser", "anna"),
                    createdBy = "demoUser",
                    createdAt = now - 200_000,
                    lastMessageText = "Can you book hotel for May 15-16?",
                    lastMessageAt = now - 100_000
                ),
                Chat(
                    id = "group_home",
                    type = ChatType.GROUP,
                    title = "Home",
                    memberIds = listOf("demoUser", "anna", "mike"),
                    createdBy = "demoUser",
                    createdAt = now - 300_000,
                    lastMessageText = "Buy sponges",
                    lastMessageAt = now - 50_000
                )
            )
        }

        private fun seedMessages(): Map<String, List<Message>> {
            val now = System.currentTimeMillis()
            return mapOf(
                "direct_anna" to listOf(
                    Message(
                        id = "m1",
                        chatId = "direct_anna",
                        senderId = "anna",
                        type = MessageType.TEXT,
                        text = "Hi!",
                        createdAt = now - 190_000
                    )
                ),
                "group_home" to listOf(
                    Message(
                        id = "m2",
                        chatId = "group_home",
                        senderId = "mike",
                        type = MessageType.TEXT,
                        text = "I will take trash today",
                        createdAt = now - 170_000
                    )
                )
            )
        }

        private fun seedQuests(): List<Quest> {
            val now = System.currentTimeMillis()
            return listOf(
                Quest(
                    id = "q1",
                    title = "Book hotel for May 15-16",
                    description = "Near downtown",
                    createdBy = "anna",
                    sourceType = QuestSourceType.DIRECT_CHAT,
                    sourceChatId = "direct_anna",
                    assignmentType = QuestAssignmentType.ASSIGNED,
                    assigneeId = "demoUser",
                    status = QuestStatus.INCOMING,
                    difficulty = QuestDifficulty.MEDIUM,
                    xpReward = 25,
                    createdAt = now - 120_000
                ),
                Quest(
                    id = "q2",
                    title = "Buy sponges",
                    createdBy = "anna",
                    sourceType = QuestSourceType.GROUP_CHAT,
                    sourceChatId = "group_home",
                    assignmentType = QuestAssignmentType.OPEN,
                    status = QuestStatus.OPEN,
                    difficulty = QuestDifficulty.EASY,
                    xpReward = 10,
                    createdAt = now - 80_000
                ),
                Quest(
                    id = "q3",
                    title = "Pay internet bill",
                    createdBy = "demoUser",
                    sourceType = QuestSourceType.PERSONAL,
                    assignmentType = QuestAssignmentType.SELF,
                    assigneeId = "demoUser",
                    status = QuestStatus.IN_PROGRESS,
                    difficulty = QuestDifficulty.EASY,
                    xpReward = 10,
                    createdAt = now - 60_000
                )
            )
        }
    }
}
