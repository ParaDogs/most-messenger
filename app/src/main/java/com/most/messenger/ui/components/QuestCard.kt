package com.most.messenger.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.most.messenger.data.model.Quest
import com.most.messenger.data.model.QuestAssignmentType
import com.most.messenger.data.model.QuestStatus

@Composable
fun QuestCard(
    quest: Quest,
    currentUserId: String,
    onAccept: (() -> Unit)? = null,
    onSave: (() -> Unit)? = null,
    onDecline: (() -> Unit)? = null,
    onClaim: (() -> Unit)? = null,
    onRelease: (() -> Unit)? = null,
    onComplete: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(quest.title, style = MaterialTheme.typography.titleMedium)
            quest.description?.let {
                Text(it, style = MaterialTheme.typography.bodyMedium)
            }
            Text("Status: ${quest.status}", style = MaterialTheme.typography.labelMedium)
            Text("XP: ${quest.xpReward}", style = MaterialTheme.typography.labelSmall)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                when {
                    quest.assignmentType == QuestAssignmentType.ASSIGNED && quest.status == QuestStatus.INCOMING && quest.assigneeId == currentUserId -> {
                        onAccept?.let { AssistChip(onClick = it, label = { Text("Accept") }) }
                        onSave?.let { AssistChip(onClick = it, label = { Text("Save") }) }
                        onDecline?.let { AssistChip(onClick = it, label = { Text("Decline") }) }
                    }
                    quest.assignmentType == QuestAssignmentType.OPEN && quest.status == QuestStatus.OPEN -> {
                        onClaim?.let { AssistChip(onClick = it, label = { Text("Claim") }) }
                    }
                    quest.assignmentType == QuestAssignmentType.OPEN && quest.status == QuestStatus.CLAIMED && quest.claimedBy == currentUserId -> {
                        onComplete?.let { AssistChip(onClick = it, label = { Text("Complete") }) }
                        onRelease?.let { AssistChip(onClick = it, label = { Text("Release") }) }
                    }
                    quest.status in setOf(QuestStatus.IN_PROGRESS, QuestStatus.CLAIMED) && (quest.assigneeId == currentUserId || quest.claimedBy == currentUserId) -> {
                        onComplete?.let { AssistChip(onClick = it, label = { Text("Complete") }) }
                    }
                }
            }
        }
    }
}
