package com.most.messenger.app

import com.most.messenger.data.repository.AuthRepository
import com.most.messenger.data.repository.ChatRepository
import com.most.messenger.data.repository.QuestRepository
import com.most.messenger.data.repository.UserRepository
import com.most.messenger.data.repository.firebase.FirebaseAuthRepository
import com.most.messenger.data.repository.firebase.FirebaseChatRepository
import com.most.messenger.data.repository.firebase.FirebaseQuestRepository
import com.most.messenger.data.repository.firebase.FirebaseUserRepository

class AppContainer {
    val authRepository: AuthRepository = FirebaseAuthRepository()
    val userRepository: UserRepository = FirebaseUserRepository(authRepository)
    val chatRepository: ChatRepository = FirebaseChatRepository()
    val questRepository: QuestRepository = FirebaseQuestRepository()
}
