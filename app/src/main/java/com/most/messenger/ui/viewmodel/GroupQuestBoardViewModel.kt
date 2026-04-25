package com.most.messenger.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.most.messenger.data.model.Quest
import com.most.messenger.data.repository.QuestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class GroupQuestBoardUiState(
    val quests: List<Quest> = emptyList(),
    val isLoading: Boolean = true
)

class GroupQuestBoardViewModel(
    chatId: String,
    questRepository: QuestRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(GroupQuestBoardUiState())
    val uiState: StateFlow<GroupQuestBoardUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            questRepository.observeGroupQuests(chatId).collect { quests ->
                _uiState.value = GroupQuestBoardUiState(quests = quests, isLoading = false)
            }
        }
    }
}
