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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.most.messenger.ui.viewmodel.ProfileSetupViewModel

@Composable
fun ProfileSetupScreen(
    viewModel: ProfileSetupViewModel,
    onProfileSaved: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) onProfileSaved()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Set up your profile", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = state.displayName,
            onValueChange = viewModel::updateDisplayName,
            label = { Text("Display name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = state.username,
            onValueChange = viewModel::updateUsername,
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = state.avatarUrl,
            onValueChange = viewModel::updateAvatarUrl,
            label = { Text("Avatar URL (optional)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        state.errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        if (state.isSaving) {
            CircularProgressIndicator()
        } else {
            Button(onClick = viewModel::saveProfile, modifier = Modifier.fillMaxWidth()) {
                Text("Save Profile")
            }
        }
    }
}
