package com.most.messenger.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.most.messenger.data.model.UserProfile
import com.most.messenger.data.repository.AuthRepository
import com.most.messenger.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileSetupUiState(
    val displayName: String = "",
    val username: String = "",
    val avatarUrl: String = "",
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val errorMessage: String? = null
)

class ProfileSetupViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileSetupUiState())
    val uiState: StateFlow<ProfileSetupUiState> = _uiState.asStateFlow()

    fun updateDisplayName(value: String) {
        _uiState.update { it.copy(displayName = value) }
    }

    fun updateUsername(value: String) {
        _uiState.update { it.copy(username = value) }
    }

    fun updateAvatarUrl(value: String) {
        _uiState.update { it.copy(avatarUrl = value) }
    }

    fun saveProfile() {
        val state = _uiState.value
        val userId = authRepository.currentUserId
        if (userId == null) {
            _uiState.update { it.copy(errorMessage = "No authenticated user") }
            return
        }
        if (state.displayName.isBlank() || state.username.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Display name and username are required") }
            return
        }

        val profile = UserProfile(
            id = userId,
            displayName = state.displayName.trim(),
            username = state.username.trim(),
            avatarUrl = state.avatarUrl.trim().ifBlank { null }
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            userRepository.createOrUpdateProfile(profile)
                .onSuccess {
                    _uiState.update { it.copy(isSaving = false, saveSuccess = true, errorMessage = null) }
                }
                .onFailure { err ->
                    _uiState.update { it.copy(isSaving = false, saveSuccess = false, errorMessage = err.message) }
                }
        }
    }
}
