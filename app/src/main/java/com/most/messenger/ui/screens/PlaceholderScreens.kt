package com.most.messenger.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.most.messenger.app.AppContainer
import com.most.messenger.data.model.ChatType
import com.most.messenger.data.model.QuestStatus
import com.most.messenger.ui.components.QuestCard
import com.most.messenger.ui.navigation.Routes
import com.most.messenger.ui.viewmodel.AppViewModelFactory
import com.most.messenger.ui.viewmodel.ChatListViewModel
import com.most.messenger.ui.viewmodel.ChatViewModel
import com.most.messenger.ui.viewmodel.ChatScopedViewModelFactory
import com.most.messenger.ui.viewmodel.DashboardTab
import com.most.messenger.ui.viewmodel.GroupQuestBoardViewModel
import com.most.messenger.ui.viewmodel.ProfileViewModel
import com.most.messenger.ui.viewmodel.QuestDashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(navController: NavHostController, container: AppContainer) {
    val viewModel: ChatListViewModel = viewModel(factory = AppViewModelFactory(container))
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(topBar = { TopAppBar(title = { Text("most") }) }) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { navController.navigate(Routes.QuestDashboard) }) { Text("Dashboard") }
                    Button(onClick = { navController.navigate(Routes.Profile) }) { Text("Profile") }
                }
            }
            items(state.chats) { chat ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val route = if (chat.type == ChatType.GROUP) "groupChat/${chat.id}" else "directChat/${chat.id}"
                        navController.navigate(route)
                    }
                ) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(chat.title.orEmpty(), style = MaterialTheme.typography.titleMedium)
                        Text(chat.lastMessageText ?: "No messages yet", style = MaterialTheme.typography.bodyMedium)
                        Text(if (chat.type == ChatType.GROUP) "Group" else "Direct", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectChatScreen(chatId: String, container: AppContainer) {
    val vm: ChatViewModel = viewModel(factory = ChatScopedViewModelFactory(container, chatId))
    ChatScreenContent(chatId = chatId, viewModel = vm, isGroup = false)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupChatScreen(chatId: String, container: AppContainer) {
    val vm: ChatViewModel = viewModel(factory = ChatScopedViewModelFactory(container, chatId))
    ChatScreenContent(chatId = chatId, viewModel = vm, isGroup = true)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatScreenContent(chatId: String, viewModel: ChatViewModel, isGroup: Boolean) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var questTitle by remember { mutableStateOf("") }

    Scaffold(topBar = { TopAppBar(title = { Text(if (isGroup) "Group chat" else "Direct chat") }) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("chatId: $chatId", style = MaterialTheme.typography.labelSmall)

            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.messages) { message ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = message.text ?: (if (message.questId != null) "Quest message" else "System"),
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                items(state.quests) { quest ->
                    QuestCard(
                        quest = quest,
                        currentUserId = "demoUser",
                        onAccept = { viewModel.acceptQuest(quest.id) },
                        onSave = { viewModel.saveQuest(quest.id) },
                        onDecline = { viewModel.declineQuest(quest.id) },
                        onClaim = { viewModel.claimQuest(quest.id) },
                        onRelease = { viewModel.releaseQuest(quest.id) },
                        onComplete = { viewModel.completeQuest(quest.id) }
                    )
                }
            }

            OutlinedTextField(
                value = questTitle,
                onValueChange = { questTitle = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(if (isGroup) "New group quest title" else "New direct quest title") }
            )
            Button(
                onClick = {
                    if (questTitle.isBlank()) return@Button
                    if (isGroup) viewModel.createGroupQuest(questTitle) else viewModel.createDirectQuest(questTitle, "demoUser")
                    questTitle = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create quest")
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = state.input,
                    onValueChange = viewModel::updateInput,
                    modifier = Modifier.weight(1f),
                    label = { Text("Message") }
                )
                Button(onClick = viewModel::sendTextMessage) { Text("Send") }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestDashboardScreen(container: AppContainer) {
    val vm: QuestDashboardViewModel = viewModel(factory = AppViewModelFactory(container))
    val state by vm.uiState.collectAsStateWithLifecycle()
    val tabs = DashboardTab.entries

    Scaffold(topBar = { TopAppBar(title = { Text("Quest dashboard") }) }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            TabRow(selectedTabIndex = tabs.indexOf(state.selectedTab)) {
                tabs.forEach { tab ->
                    Tab(selected = tab == state.selectedTab, onClick = { vm.selectTab(tab) }, text = { Text(tab.name) })
                }
            }
            LazyColumn(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.quests) { quest ->
                    QuestCard(quest = quest, currentUserId = "demoUser")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(container: AppContainer) {
    val vm: ProfileViewModel = viewModel(factory = AppViewModelFactory(container))
    val state by vm.uiState.collectAsStateWithLifecycle()

    Scaffold(topBar = { TopAppBar(title = { Text("Profile") }) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(state.profile?.displayName ?: "Demo user", style = MaterialTheme.typography.headlineSmall)
            Text("@${state.profile?.username ?: "demo"}")
            Text("Completed quests: ${state.completedCount}")
            Text("Created quests: ${state.createdCount}")
            Text("XP: ${state.profile?.xp ?: state.completedCount * 10}")
            Button(onClick = vm::signOut) { Text("Sign out") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupQuestBoardScreen(chatId: String, container: AppContainer) {
    val vm: GroupQuestBoardViewModel = viewModel(factory = ChatScopedViewModelFactory(container, chatId))
    val state by vm.uiState.collectAsStateWithLifecycle()

    Scaffold(topBar = { TopAppBar(title = { Text("Group quest board") }) }) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.quests) { quest ->
                val statusPrefix = when (quest.status) {
                    QuestStatus.OPEN -> "Open"
                    QuestStatus.CLAIMED -> "Claimed"
                    QuestStatus.COMPLETED -> "Completed"
                    else -> quest.status.name
                }
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp)) {
                        Text("$statusPrefix • ${quest.title}", style = MaterialTheme.typography.titleMedium)
                        quest.claimedBy?.let { Text("Claimed by: $it") }
                    }
                }
            }
        }
    }
}
