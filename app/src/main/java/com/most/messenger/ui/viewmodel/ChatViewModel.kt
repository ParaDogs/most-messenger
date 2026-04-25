package com.most.messenger.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.most.messenger.data.model.CreateQuestInput
import com.most.messenger.data.model.Message
import com.most.messenger.data.model.Quest
import com.most.messenger.data.model.QuestDifficulty
import com.most.messenger.data.repository.AuthRepository
import com.most.messenger.data.repository.ChatRepository
import com.most.messenger.data.repository.QuestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val quests: List<Quest> = emptyList(),
    val input: String = "",
    val isLoading: Boolean = true
)

class ChatViewModel(
    private val chatId: String,
    private val authRepository: AuthRepository,
    private val chatRepository: ChatRepository,
    private val questRepository: QuestRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        val userId = authRepository.currentUserId ?: "demoUser"
        viewModelScope.launch {
            combine(
                chatRepository.observeMessages(chatId),
                questRepository.observeDashboardQuests(userId)
            ) { messages, quests ->
                val linked = quests.filter { it.sourceChatId == chatId }
                ChatUiState(messages = messages, quests = linked, input = _uiState.value.input, isLoading = false)
            }.collect { _uiState.value = it }
        }
    }

    fun updateInput(value: String) {
        _uiState.update { it.copy(input = value) }
    }

    fun sendTextMessage() {
        val text = _uiState.value.input
        if (text.isBlank()) return
        viewModelScope.launch {
            chatRepository.sendTextMessage(chatId, text)
            _uiState.update { it.copy(input = "") }
        }
    }

    fun createGroupQuest(title: String) {
        viewModelScope.launch {
            questRepository.createOpenGroupQuest(
                chatId = chatId,
                input = CreateQuestInput(title = title, difficulty = QuestDifficulty.EASY)
            )
        }
    }

    fun createDirectQuest(title: String, recipientId: String) {
        viewModelScope.launch {
            questRepository.createDirectQuest(
                chatId = chatId,
                recipientId = recipientId,
                input = CreateQuestInput(title = title, difficulty = QuestDifficulty.MEDIUM)
            )
        }
    }

    fun acceptQuest(questId: String) {
        viewModelScope.launch { questRepository.acceptQuest(questId) }
    }

    fun saveQuest(questId: String) {
        viewModelScope.launch { questRepository.saveQuestForLater(questId) }
    }

    fun declineQuest(questId: String) {
        viewModelScope.launch { questRepository.declineQuest(questId) }
    }

    fun claimQuest(questId: String) {
        viewModelScope.launch { questRepository.claimQuest(questId) }
    }

    fun releaseQuest(questId: String) {
        viewModelScope.launch { questRepository.releaseQuest(questId) }
    }

    fun completeQuest(questId: String) {
        viewModelScope.launch { questRepository.completeQuest(questId) }
    }
}
