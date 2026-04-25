package com.most.messenger.ui.components

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
    // TODO(step-4): Render role-based quest actions depending on status and assignment type.
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(quest.title, style = MaterialTheme.typography.titleMedium)
            quest.description?.let {
                Text(it, style = MaterialTheme.typography.bodyMedium)
            }
            Text("Status: ${quest.status}", style = MaterialTheme.typography.labelMedium)
        }
    }
}
