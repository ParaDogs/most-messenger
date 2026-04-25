package com.most.messenger.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.most.messenger.data.model.QuestStatus
import com.most.messenger.data.model.UserProfile
import com.most.messenger.data.repository.AuthRepository
import com.most.messenger.data.repository.QuestRepository
import com.most.messenger.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val profile: UserProfile? = null,
    val completedCount: Int = 0,
    val createdCount: Int = 0,
    val isLoading: Boolean = true
)

class ProfileViewModel(
    private val authRepository: AuthRepository,
    userRepository: UserRepository,
    questRepository: QuestRepository
) : ViewModel() {
    private val uid = authRepository.currentUserId ?: "demoUser"
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.observeUserProfile(uid).collect { profile ->
                _uiState.value = _uiState.value.copy(profile = profile, isLoading = false)
            }
        }
        viewModelScope.launch {
            questRepository.observeDashboardQuests(uid).collect { quests ->
                _uiState.value = _uiState.value.copy(
                    completedCount = quests.count { it.status == QuestStatus.COMPLETED },
                    createdCount = quests.count { it.createdBy == uid },
                    isLoading = false
                )
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
    }
}
