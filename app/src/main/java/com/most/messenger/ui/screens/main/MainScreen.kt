package com.most.messenger.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.most.messenger.ui.navigation.AppRoutes

@Composable
fun MainScreen(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("most MVP main")
        Text("Current route: ${currentRoute.orEmpty()}")
        Button(onClick = { onNavigate(AppRoutes.CHAT_LIST) }, modifier = Modifier.fillMaxWidth()) { Text("Chats") }
        Button(onClick = { onNavigate(AppRoutes.QUEST_DASHBOARD) }, modifier = Modifier.fillMaxWidth()) { Text("Quest Dashboard") }
        Button(onClick = { onNavigate(AppRoutes.PROFILE) }, modifier = Modifier.fillMaxWidth()) { Text("Profile") }
        // TODO Step 3: replace buttons with bottom navigation scaffold.
    }
}
