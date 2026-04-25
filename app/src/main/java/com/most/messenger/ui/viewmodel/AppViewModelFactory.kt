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
            ChatListViewModel::class.java -> ChatListViewModel(container.chatRepository) as T
            CreateQuestViewModel::class.java -> CreateQuestViewModel(container.questRepository) as T
            QuestDashboardViewModel::class.java -> QuestDashboardViewModel(
                authRepository = container.authRepository,
                questRepository = container.questRepository
            ) as T
            ProfileViewModel::class.java -> ProfileViewModel(
                authRepository = container.authRepository,
                userRepository = container.userRepository,
                questRepository = container.questRepository
            ) as T
            else -> error("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

class ChatScopedViewModelFactory(
    private val container: AppContainer,
    private val chatId: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ChatViewModel::class.java -> ChatViewModel(
                chatId = chatId,
                authRepository = container.authRepository,
                chatRepository = container.chatRepository,
                questRepository = container.questRepository
            ) as T
            GroupQuestBoardViewModel::class.java -> GroupQuestBoardViewModel(
                chatId = chatId,
                questRepository = container.questRepository
            ) as T
            else -> error("Unknown scoped ViewModel class: ${modelClass.name}")
        }
    }
}
