package com.most.messenger.data.repository.firebase

import com.most.messenger.data.model.CreateQuestInput
import com.most.messenger.data.model.Quest
import com.most.messenger.data.repository.QuestRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FirebaseQuestRepository : QuestRepository {
    override suspend fun createDirectQuest(chatId: String, recipientId: String, input: CreateQuestInput): Result<String> {
        // TODO(step-4): Create quest doc and linked QUEST message in direct chat.
        return Result.success("")
    }

    override suspend fun createOpenGroupQuest(chatId: String, input: CreateQuestInput): Result<String> {
        // TODO(step-4): Create OPEN group quest and linked QUEST message.
        return Result.success("")
    }

    override suspend fun createPersonalQuest(input: CreateQuestInput): Result<String> {
        // TODO(step-4): Create private self quest.
        return Result.success("")
    }

    override fun observeDashboardQuests(userId: String): Flow<List<Quest>> {
        // TODO(step-5): Aggregate direct/group/personal quests by dashboard filters.
        return flowOf(emptyList())
    }

    override fun observeGroupQuests(chatId: String): Flow<List<Quest>> {
        // TODO(step-5): Observe quests bound to group chat.
        return flowOf(emptyList())
    }

    override suspend fun acceptQuest(questId: String): Result<Unit> {
        // TODO(step-4): INCOMING -> IN_PROGRESS.
        return Result.success(Unit)
    }

    override suspend fun declineQuest(questId: String): Result<Unit> {
        // TODO(step-4): INCOMING -> DECLINED. No XP penalty.
        return Result.success(Unit)
    }

    override suspend fun saveQuestForLater(questId: String): Result<Unit> {
        // TODO(step-4): INCOMING -> SAVED. Private action.
        return Result.success(Unit)
    }

    override suspend fun claimQuest(questId: String): Result<Unit> {
        // TODO(step-4): OPEN -> CLAIMED with claimedBy.
        return Result.success(Unit)
    }

    override suspend fun releaseQuest(questId: String): Result<Unit> {
        // TODO(step-4): CLAIMED -> OPEN and clear claimedBy.
        return Result.success(Unit)
    }

    override suspend fun completeQuest(questId: String): Result<Unit> {
        // TODO(step-6): Mark completed and update XP/profile counters.
        return Result.success(Unit)
    }
}
