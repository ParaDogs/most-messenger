package com.most.messenger.data.repository.inmemory

import com.most.messenger.data.model.CreateQuestInput
import com.most.messenger.data.model.Quest
import com.most.messenger.data.model.QuestAssignmentType
import com.most.messenger.data.model.QuestSourceType
import com.most.messenger.data.model.QuestStatus
import com.most.messenger.data.model.calculateXpReward
import com.most.messenger.data.repository.AuthRepository
import com.most.messenger.data.repository.QuestRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class InMemoryQuestRepository(
    private val authRepository: AuthRepository,
    private val store: InMemoryMostStore
) : QuestRepository {

    override suspend fun createDirectQuest(chatId: String, recipientId: String, input: CreateQuestInput): Result<String> = runCatching {
        val uid = authRepository.currentUserId ?: "demoUser"
        val quest = Quest(
            id = UUID.randomUUID().toString(),
            title = input.title,
            description = input.description,
            createdBy = uid,
            sourceType = QuestSourceType.DIRECT_CHAT,
            sourceChatId = chatId,
            assignmentType = QuestAssignmentType.ASSIGNED,
            assigneeId = recipientId,
            status = QuestStatus.INCOMING,
            category = input.category,
            difficulty = input.difficulty,
            deadlineAt = input.deadlineAt,
            requiresApproval = input.requiresApproval,
            rewardText = input.rewardText,
            xpReward = calculateXpReward(input.difficulty),
            createdAt = System.currentTimeMillis()
        )
        store.addQuestAndLinkedMessage(quest)
        quest.id
    }

    override suspend fun createOpenGroupQuest(chatId: String, input: CreateQuestInput): Result<String> = runCatching {
        val uid = authRepository.currentUserId ?: "demoUser"
        val quest = Quest(
            id = UUID.randomUUID().toString(),
            title = input.title,
            description = input.description,
            createdBy = uid,
            sourceType = QuestSourceType.GROUP_CHAT,
            sourceChatId = chatId,
            assignmentType = QuestAssignmentType.OPEN,
            status = QuestStatus.OPEN,
            category = input.category,
            difficulty = input.difficulty,
            deadlineAt = input.deadlineAt,
            requiresApproval = input.requiresApproval,
            rewardText = input.rewardText,
            xpReward = calculateXpReward(input.difficulty),
            createdAt = System.currentTimeMillis()
        )
        store.addQuestAndLinkedMessage(quest)
        quest.id
    }

    override suspend fun createPersonalQuest(input: CreateQuestInput): Result<String> = runCatching {
        val uid = authRepository.currentUserId ?: "demoUser"
        val quest = Quest(
            id = UUID.randomUUID().toString(),
            title = input.title,
            description = input.description,
            createdBy = uid,
            sourceType = QuestSourceType.PERSONAL,
            assignmentType = QuestAssignmentType.SELF,
            assigneeId = uid,
            status = QuestStatus.IN_PROGRESS,
            category = input.category,
            difficulty = input.difficulty,
            deadlineAt = input.deadlineAt,
            requiresApproval = input.requiresApproval,
            rewardText = input.rewardText,
            xpReward = calculateXpReward(input.difficulty),
            createdAt = System.currentTimeMillis()
        )
        store.addQuestAndLinkedMessage(quest)
        quest.id
    }

    override fun observeDashboardQuests(userId: String): Flow<List<Quest>> {
        return store.quests.map { quests ->
            quests.filter { quest ->
                quest.createdBy == userId || quest.assigneeId == userId || quest.claimedBy == userId ||
                    (quest.assignmentType == QuestAssignmentType.OPEN && quest.sourceChatId != null)
            }.sortedByDescending { it.createdAt }
        }
    }

    override fun observeGroupQuests(chatId: String): Flow<List<Quest>> {
        return store.quests.map { quests ->
            quests.filter { it.sourceChatId == chatId && it.sourceType == QuestSourceType.GROUP_CHAT }
                .sortedByDescending { it.createdAt }
        }
    }

    override suspend fun acceptQuest(questId: String): Result<Unit> = runCatching {
        store.updateQuest(questId) { it.copy(status = QuestStatus.IN_PROGRESS, assigneeId = authRepository.currentUserId ?: "demoUser") }
    }

    override suspend fun declineQuest(questId: String): Result<Unit> = runCatching {
        store.updateQuest(questId) { it.copy(status = QuestStatus.DECLINED) }
    }

    override suspend fun saveQuestForLater(questId: String): Result<Unit> = runCatching {
        store.updateQuest(questId) { it.copy(status = QuestStatus.SAVED) }
    }

    override suspend fun claimQuest(questId: String): Result<Unit> = runCatching {
        val uid = authRepository.currentUserId ?: "demoUser"
        store.updateQuest(questId) { it.copy(status = QuestStatus.CLAIMED, claimedBy = uid) }
    }

    override suspend fun releaseQuest(questId: String): Result<Unit> = runCatching {
        store.updateQuest(questId) { it.copy(status = QuestStatus.OPEN, claimedBy = null) }
    }

    override suspend fun completeQuest(questId: String): Result<Unit> = runCatching {
        store.updateQuest(questId) { it.copy(status = QuestStatus.COMPLETED, completedAt = System.currentTimeMillis()) }
    }
}
