package com.most.messenger.data.model

enum class ChatType {
    DIRECT,
    GROUP
}

enum class MessageType {
    TEXT,
    QUEST,
    SYSTEM
}

enum class QuestSourceType {
    DIRECT_CHAT,
    GROUP_CHAT,
    PERSONAL
}

enum class QuestAssignmentType {
    ASSIGNED,
    OPEN,
    SELF
}

enum class QuestStatus {
    INCOMING,
    SAVED,
    OPEN,
    CLAIMED,
    IN_PROGRESS,
    COMPLETED,
    DECLINED,
    CANCELLED
}

enum class QuestDifficulty {
    EASY,
    MEDIUM,
    HARD,
    EPIC
}
