package com.most.messenger.ui.screens.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.most.messenger.data.model.Quest

@Composable
fun QuestCard(
    quest: Quest,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = quest.title, style = MaterialTheme.typography.titleMedium)
            quest.description?.let { Text(text = it, style = MaterialTheme.typography.bodyMedium) }
            Text(text = "Status: ${quest.status}", style = MaterialTheme.typography.labelMedium)
            // TODO Step 4: render actions based on quest status/assignment/current user role.
        }
    }
}
