package com.most.messenger.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.most.messenger.ui.navigation.Routes

@Composable
fun ChatListScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
    ) {
        Text("Chat List (Step 3 TODO)", style = MaterialTheme.typography.headlineSmall)
        Button(onClick = { navController.navigate(Routes.QuestDashboard) }, modifier = Modifier.fillMaxWidth()) {
            Text("Open Quest Dashboard")
        }
        Button(onClick = { navController.navigate(Routes.Profile) }, modifier = Modifier.fillMaxWidth()) {
            Text("Open Profile")
        }
    }
}

@Composable
fun DirectChatScreen(chatId: String) {
    PlaceholderScreen(title = "Direct Chat ($chatId)", subtitle = "TODO Step 3+4")
}

@Composable
fun GroupChatScreen(chatId: String) {
    PlaceholderScreen(title = "Group Chat ($chatId)", subtitle = "TODO Step 3+4")
}

@Composable
fun QuestDashboardScreen() {
    PlaceholderScreen(title = "Quest Dashboard", subtitle = "TODO Step 5")
}

@Composable
fun ProfileScreen() {
    PlaceholderScreen(title = "Profile", subtitle = "TODO Step 6")
}

@Composable
fun GroupQuestBoardScreen(chatId: String) {
    PlaceholderScreen(title = "Group Quest Board ($chatId)", subtitle = "TODO Step 5")
}

@Composable
private fun PlaceholderScreen(title: String, subtitle: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(title, style = MaterialTheme.typography.headlineSmall)
        Text(subtitle, style = MaterialTheme.typography.bodyLarge)
    }
}
