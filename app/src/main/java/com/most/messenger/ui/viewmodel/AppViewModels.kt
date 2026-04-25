package com.most.messenger.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.most.messenger.data.model.UserProfile
import com.most.messenger.data.repository.AuthRepository
import com.most.messenger.data.repository.UserRepository
import com.most.messenger.data.repository.impl.FirebaseAuthRepository
import com.most.messenger.data.repository.impl.FirebaseUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AuthUiState {
    data object Idle : AuthUiState
    data object Loading : AuthUiState
    data class Success(val userId: String) : AuthUiState
    data class Error(val message: String) : AuthUiState
}

class AuthViewModel(
    private val authRepository: AuthRepository = FirebaseAuthRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun signIn(email: String, password: String) {
        submitAuthAction { authRepository.signIn(email, password) }
    }

    fun signUp(email: String, password: String) {
        submitAuthAction { authRepository.signUp(email, password) }
    }

    fun currentUserId(): String? = authRepository.currentUserId()

    private fun submitAuthAction(action: suspend () -> Result<Unit>) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            action().fold(
                onSuccess = {
                    _uiState.value = AuthUiState.Success(authRepository.currentUserId().orEmpty())
                },
                onFailure = {
                    _uiState.value = AuthUiState.Error(it.message ?: "Authentication failed")
                }
            )
        }
    }
}

sealed interface ProfileSetupUiState {
    data object Idle : ProfileSetupUiState
    data object Loading : ProfileSetupUiState
    data object Saved : ProfileSetupUiState
    data class Error(val message: String) : ProfileSetupUiState
}

class ProfileSetupViewModel(
    private val authRepository: AuthRepository = FirebaseAuthRepository(),
    private val userRepository: UserRepository = FirebaseUserRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileSetupUiState>(ProfileSetupUiState.Idle)
    val uiState: StateFlow<ProfileSetupUiState> = _uiState.asStateFlow()

    fun saveProfile(displayName: String, username: String, avatarUrl: String?) {
        viewModelScope.launch {
            _uiState.value = ProfileSetupUiState.Loading
            val uid = authRepository.currentUserId()
            if (uid == null) {
                _uiState.value = ProfileSetupUiState.Error("No authenticated user")
                return@launch
            }
            val profile = UserProfile(id = uid, displayName = displayName, username = username, avatarUrl = avatarUrl)
            userRepository.createOrUpdateProfile(profile).fold(
                onSuccess = { _uiState.value = ProfileSetupUiState.Saved },
                onFailure = { _uiState.value = ProfileSetupUiState.Error(it.message ?: "Failed to save profile") }
            )
        }
    }
}

class ChatListViewModel : ViewModel() // TODO Step 3
class ChatViewModel : ViewModel() // TODO Step 3 + Step 4
class CreateQuestViewModel : ViewModel() // TODO Step 4
class QuestDashboardViewModel : ViewModel() // TODO Step 5
class GroupQuestBoardViewModel : ViewModel() // TODO Step 5
class ProfileViewModel : ViewModel() // TODO Step 6
