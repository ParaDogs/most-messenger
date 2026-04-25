package com.most.messenger.data.model

enum class ChatType { DIRECT, GROUP }

enum class MessageType { TEXT, QUEST, SYSTEM }

enum class QuestSourceType { DIRECT_CHAT, GROUP_CHAT, PERSONAL }

enum class QuestAssignmentType { ASSIGNED, OPEN, SELF }

enum class QuestStatus { INCOMING, SAVED, OPEN, CLAIMED, IN_PROGRESS, COMPLETED, DECLINED, CANCELLED }

enum class QuestDifficulty { EASY, MEDIUM, HARD, EPIC }

data class UserProfile(
    val id: String = "",
    val username: String = "",
    val displayName: String = "",
    val avatarUrl: String? = null,
    val xp: Int = 0,
    val level: Int = 1,
    val completedQuestCount: Int = 0,
    val createdQuestCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

data class Chat(
    val id: String = "",
    val type: ChatType = ChatType.DIRECT,
    val title: String? = null,
    val memberIds: List<String> = emptyList(),
    val createdBy: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val lastMessageText: String? = null,
    val lastMessageAt: Long? = null
)

data class Message(
    val id: String = "",
    val chatId: String = "",
    val senderId: String = "",
    val type: MessageType = MessageType.TEXT,
    val text: String? = null,
    val questId: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

data class Quest(
    val id: String = "",
    val title: String = "",
    val description: String? = null,
    val createdBy: String = "",
    val sourceType: QuestSourceType = QuestSourceType.PERSONAL,
    val sourceChatId: String? = null,
    val assignmentType: QuestAssignmentType = QuestAssignmentType.SELF,
    val assigneeId: String? = null,
    val claimedBy: String? = null,
    val status: QuestStatus = QuestStatus.OPEN,
    val category: String? = null,
    val difficulty: QuestDifficulty = QuestDifficulty.EASY,
    val deadlineAt: Long? = null,
    val requiresApproval: Boolean = false,
    val rewardText: String? = null,
    val xpReward: Int = 10,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
)

data class QuestInput(
    val title: String,
    val description: String? = null,
    val deadlineAt: Long? = null,
    val rewardText: String? = null,
    val category: String? = null,
    val difficulty: QuestDifficulty = QuestDifficulty.EASY,
    val requiresApproval: Boolean = false
)
