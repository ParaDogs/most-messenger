package com.most.messenger.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.most.messenger.data.repository.AuthRepository
import com.most.messenger.data.repository.firebase.mapFirebaseAuthError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isAuthenticated: Boolean = false,
    val userId: String? = null
)

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AuthUiState(
            isAuthenticated = authRepository.currentUserId != null,
            userId = authRepository.currentUserId
        )
    )
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun updateEmail(value: String) {
        _uiState.update { it.copy(email = value) }
    }

    fun updatePassword(value: String) {
        _uiState.update { it.copy(password = value) }
    }

    fun signIn() {
        submitAuth { email, password -> authRepository.signIn(email, password) }
    }

    fun signUp() {
        submitAuth { email, password -> authRepository.signUp(email, password) }
    }

    private fun submitAuth(action: suspend (String, String) -> Result<String>) {
        val state = _uiState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Email and password are required") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = action(state.email.trim(), state.password)
            result
                .onSuccess { uid ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isAuthenticated = true,
                            userId = uid,
                            errorMessage = null
                        )
                    }
                }
                .onFailure { err ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isAuthenticated = false,
                            errorMessage = mapFirebaseAuthError(err)
                        )
                    }
                }
        }
    }

    fun signOut() {
        authRepository.signOut()
        _uiState.update {
            it.copy(isAuthenticated = false, userId = null, password = "")
        }
    }
}
