package com.most.messenger.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.most.messenger.app.AppContainer

class AppViewModelFactory(
    private val container: AppContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            AuthViewModel::class.java -> AuthViewModel(container.authRepository) as T
            ProfileSetupViewModel::class.java -> ProfileSetupViewModel(
                authRepository = container.authRepository,
                userRepository = container.userRepository
            ) as T
            else -> error("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
