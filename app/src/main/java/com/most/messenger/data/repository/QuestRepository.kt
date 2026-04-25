package com.most.messenger.data.repository

import com.most.messenger.data.model.CreateQuestInput
import com.most.messenger.data.model.Quest
import kotlinx.coroutines.flow.Flow

interface QuestRepository {
    suspend fun createDirectQuest(chatId: String, recipientId: String, input: CreateQuestInput): Result<String>
    suspend fun createOpenGroupQuest(chatId: String, input: CreateQuestInput): Result<String>
    suspend fun createPersonalQuest(input: CreateQuestInput): Result<String>

    fun observeDashboardQuests(userId: String): Flow<List<Quest>>
    fun observeGroupQuests(chatId: String): Flow<List<Quest>>

    suspend fun acceptQuest(questId: String): Result<Unit>
    suspend fun declineQuest(questId: String): Result<Unit>
    suspend fun saveQuestForLater(questId: String): Result<Unit>
    suspend fun claimQuest(questId: String): Result<Unit>
    suspend fun releaseQuest(questId: String): Result<Unit>
    suspend fun completeQuest(questId: String): Result<Unit>
}
