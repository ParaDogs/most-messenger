package com.most.messenger.app

import com.most.messenger.data.repository.AuthRepository
import com.most.messenger.data.repository.ChatRepository
import com.most.messenger.data.repository.QuestRepository
import com.most.messenger.data.repository.UserRepository
import com.most.messenger.data.repository.firebase.FirebaseAuthRepository
import com.most.messenger.data.repository.firebase.FirebaseUserRepository
import com.most.messenger.data.repository.inmemory.InMemoryChatRepository
import com.most.messenger.data.repository.inmemory.InMemoryMostStore
import com.most.messenger.data.repository.inmemory.InMemoryQuestRepository

class AppContainer {
    val authRepository: AuthRepository = FirebaseAuthRepository()
    val userRepository: UserRepository = FirebaseUserRepository(authRepository)

    private val inMemoryStore = InMemoryMostStore()
    val chatRepository: ChatRepository = InMemoryChatRepository(authRepository, inMemoryStore)
    val questRepository: QuestRepository = InMemoryQuestRepository(authRepository, inMemoryStore)
}
