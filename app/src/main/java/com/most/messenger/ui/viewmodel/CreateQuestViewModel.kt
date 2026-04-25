package com.most.messenger.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.most.messenger.data.model.CreateQuestInput
import com.most.messenger.data.model.QuestDifficulty
import com.most.messenger.data.repository.QuestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CreateQuestUiState(
    val title: String = "",
    val description: String = "",
    val isSaving: Boolean = false,
    val saved: Boolean = false
)

class CreateQuestViewModel(
    private val questRepository: QuestRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateQuestUiState())
    val uiState: StateFlow<CreateQuestUiState> = _uiState.asStateFlow()

    fun updateTitle(value: String) = _uiState.update { it.copy(title = value) }
    fun updateDescription(value: String) = _uiState.update { it.copy(description = value) }

    fun createPersonalQuest() {
        val state = _uiState.value
        if (state.title.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            questRepository.createPersonalQuest(
                CreateQuestInput(
                    title = state.title,
                    description = state.description.ifBlank { null },
                    difficulty = QuestDifficulty.EASY
                )
            )
            _uiState.update { it.copy(isSaving = false, saved = true, title = "", description = "") }
        }
    }
}
