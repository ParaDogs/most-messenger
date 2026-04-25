package com.most.messenger.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.most.messenger.app.AppContainer
import com.most.messenger.ui.screens.auth.AuthScreen
import com.most.messenger.ui.screens.ChatListScreen
import com.most.messenger.ui.screens.DirectChatScreen
import com.most.messenger.ui.screens.GroupChatScreen
import com.most.messenger.ui.screens.GroupQuestBoardScreen
import com.most.messenger.ui.screens.ProfileScreen
import com.most.messenger.ui.screens.profile.ProfileSetupScreen
import com.most.messenger.ui.screens.QuestDashboardScreen
import com.most.messenger.ui.viewmodel.AppViewModelFactory
import com.most.messenger.ui.viewmodel.AuthViewModel
import com.most.messenger.ui.viewmodel.ProfileSetupViewModel

@Composable
fun MostApp() {
    val navController = rememberNavController()
    val container = remember { AppContainer() }
    val viewModelFactory = remember { AppViewModelFactory(container) }

    NavHost(navController = navController, startDestination = Routes.Auth) {
        composable(Routes.Auth) {
            val viewModel: AuthViewModel = viewModel(factory = viewModelFactory)
            AuthScreen(
                viewModel = viewModel,
                onAuthenticated = {
                    navController.navigate(Routes.ProfileSetup) {
                        popUpTo(Routes.Auth) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.ProfileSetup) {
            val viewModel: ProfileSetupViewModel = viewModel(factory = viewModelFactory)
            ProfileSetupScreen(
                viewModel = viewModel,
                onProfileSaved = {
                    navController.navigate(Routes.ChatList) {
                        popUpTo(Routes.ProfileSetup) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.Main) { ChatListScreen(navController) }
        composable(Routes.ChatList) { ChatListScreen(navController) }
        composable(
            route = Routes.DirectChat,
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
        ) {
            DirectChatScreen(chatId = it.arguments?.getString("chatId").orEmpty())
        }
        composable(
            route = Routes.GroupChat,
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
        ) {
            GroupChatScreen(chatId = it.arguments?.getString("chatId").orEmpty())
        }
        composable(Routes.QuestDashboard) { QuestDashboardScreen() }
        composable(Routes.Profile) { ProfileScreen() }
        composable(
            route = Routes.GroupQuestBoard,
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
        ) {
            GroupQuestBoardScreen(chatId = it.arguments?.getString("chatId").orEmpty())
        }
    }
}
