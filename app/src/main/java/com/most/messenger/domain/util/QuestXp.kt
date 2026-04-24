package com.most.messenger.domain.util

import com.most.messenger.data.model.QuestDifficulty

fun calculateXpReward(difficulty: QuestDifficulty): Int = when (difficulty) {
    QuestDifficulty.EASY -> 10
    QuestDifficulty.MEDIUM -> 25
    QuestDifficulty.HARD -> 60
    QuestDifficulty.EPIC -> 150
}
