package com.most.messenger.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.most.messenger.data.model.Quest
import com.most.messenger.data.model.QuestAssignmentType
import com.most.messenger.data.model.QuestStatus
import com.most.messenger.data.repository.AuthRepository
import com.most.messenger.data.repository.QuestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class DashboardTab {
    INCOMING,
    SAVED,
    IN_PROGRESS,
    OPEN_GROUP,
    PERSONAL,
    CREATED,
    COMPLETED
}

data class QuestDashboardUiState(
    val selectedTab: DashboardTab = DashboardTab.INCOMING,
    val quests: List<Quest> = emptyList(),
    val isLoading: Boolean = true
)

class QuestDashboardViewModel(
    authRepository: AuthRepository,
    questRepository: QuestRepository
) : ViewModel() {
    private val userId = authRepository.currentUserId ?: "demoUser"
    private var allQuests: List<Quest> = emptyList()

    private val _uiState = MutableStateFlow(QuestDashboardUiState())
    val uiState: StateFlow<QuestDashboardUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            questRepository.observeDashboardQuests(userId).collect { quests ->
                allQuests = quests
                applyFilter()
            }
        }
    }

    fun selectTab(tab: DashboardTab) {
        _uiState.update { it.copy(selectedTab = tab) }
        applyFilter()
    }

    private fun applyFilter() {
        val filtered = when (_uiState.value.selectedTab) {
            DashboardTab.INCOMING -> allQuests.filter { it.status == QuestStatus.INCOMING }
            DashboardTab.SAVED -> allQuests.filter { it.status == QuestStatus.SAVED }
            DashboardTab.IN_PROGRESS -> allQuests.filter { it.status in setOf(QuestStatus.IN_PROGRESS, QuestStatus.CLAIMED) }
            DashboardTab.OPEN_GROUP -> allQuests.filter {
                it.assignmentType == QuestAssignmentType.OPEN && it.status == QuestStatus.OPEN
            }
            DashboardTab.PERSONAL -> allQuests.filter { it.assignmentType == QuestAssignmentType.SELF }
            DashboardTab.CREATED -> allQuests.filter { it.createdBy == userId }
            DashboardTab.COMPLETED -> allQuests.filter { it.status == QuestStatus.COMPLETED }
        }
        _uiState.update { it.copy(quests = filtered, isLoading = false) }
    }
}
