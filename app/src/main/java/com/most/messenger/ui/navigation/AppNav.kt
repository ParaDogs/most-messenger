package com.most.messenger.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.most.messenger.ui.screens.auth.AuthScreen
import com.most.messenger.ui.screens.chat.ChatListScreen
import com.most.messenger.ui.screens.chat.DirectChatScreen
import com.most.messenger.ui.screens.chat.GroupChatScreen
import com.most.messenger.ui.screens.createquest.GroupQuestBoardScreen
import com.most.messenger.ui.screens.dashboard.QuestDashboardScreen
import com.most.messenger.ui.screens.main.MainScreen
import com.most.messenger.ui.screens.profile.ProfileScreen
import com.most.messenger.ui.screens.profile.ProfileSetupScreen
import com.most.messenger.ui.viewmodel.AuthViewModel

object AppRoutes {
    const val AUTH = "auth"
    const val PROFILE_SETUP = "profileSetup"
    const val MAIN = "main"
    const val CHAT_LIST = "chatList"
    const val DIRECT_CHAT = "directChat/{chatId}"
    const val GROUP_CHAT = "groupChat/{chatId}"
    const val QUEST_DASHBOARD = "questDashboard"
    const val PROFILE = "profile"
    const val GROUP_QUEST_BOARD = "groupQuestBoard/{chatId}"
}

@Composable
fun MostAppNavHost() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val backstackEntry by navController.currentBackStackEntryAsState()

    val startDestination = if (authViewModel.currentUserId() == null) AppRoutes.AUTH else AppRoutes.MAIN

    NavHost(navController = navController, startDestination = startDestination) {
        composable(AppRoutes.AUTH) {
            AuthScreen(
                viewModel = authViewModel,
                onAuthSuccess = { navController.navigate(AppRoutes.PROFILE_SETUP) }
            )
        }
        composable(AppRoutes.PROFILE_SETUP) {
            ProfileSetupScreen(onDone = { navController.navigate(AppRoutes.MAIN) })
        }
        composable(AppRoutes.MAIN) {
            MainScreen(
                currentRoute = backstackEntry?.destination?.route,
                onNavigate = { navController.navigate(it) }
            )
        }
        composable(AppRoutes.CHAT_LIST) { ChatListScreen() }
        composable(
            route = AppRoutes.DIRECT_CHAT,
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
        ) {
            DirectChatScreen(chatId = it.arguments?.getString("chatId").orEmpty())
        }
        composable(
            route = AppRoutes.GROUP_CHAT,
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
        ) {
            GroupChatScreen(chatId = it.arguments?.getString("chatId").orEmpty())
        }
        composable(AppRoutes.QUEST_DASHBOARD) { QuestDashboardScreen() }
        composable(AppRoutes.PROFILE) { ProfileScreen() }
        composable(
            route = AppRoutes.GROUP_QUEST_BOARD,
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
        ) {
            GroupQuestBoardScreen(chatId = it.arguments?.getString("chatId").orEmpty())
        }
    }
}
