package com.most.messenger.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.most.messenger.ui.viewmodel.ProfileSetupUiState
import com.most.messenger.ui.viewmodel.ProfileSetupViewModel

@Composable
fun ProfileSetupScreen(
    onDone: () -> Unit,
    viewModel: ProfileSetupViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var displayName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var avatarUrl by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        if (uiState is ProfileSetupUiState.Saved) onDone()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Setup profile", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(value = displayName, onValueChange = { displayName = it }, label = { Text("Display name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = avatarUrl, onValueChange = { avatarUrl = it }, label = { Text("Avatar URL (optional)") }, modifier = Modifier.fillMaxWidth())

        Button(onClick = {
            viewModel.saveProfile(displayName, username, avatarUrl.ifBlank { null })
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Continue")
        }

        when (val state = uiState) {
            is ProfileSetupUiState.Error -> Text(state.message, color = MaterialTheme.colorScheme.error)
            ProfileSetupUiState.Loading -> CircularProgressIndicator()
            else -> Unit
        }
    }
}
