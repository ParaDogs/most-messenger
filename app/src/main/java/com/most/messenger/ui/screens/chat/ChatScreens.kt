package com.most.messenger.ui.screens.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChatListScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Chat list placeholder")
        Text("TODO Step 3: load direct/group chats from ChatRepository")
    }
}

@Composable
fun DirectChatScreen(chatId: String) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Direct chat: $chatId")
        Text("TODO Step 3: send/observe text messages")
        Text("TODO Step 4: send direct quests and render QuestCard")
    }
}

@Composable
fun GroupChatScreen(chatId: String) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Group chat: $chatId")
        Text("TODO Step 3: show group messages")
        Text("TODO Step 4: create open group quests")
    }
}
