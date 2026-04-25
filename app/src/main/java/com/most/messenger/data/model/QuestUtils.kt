package com.most.messenger.data.model

fun calculateXpReward(difficulty: QuestDifficulty): Int = when (difficulty) {
    QuestDifficulty.EASY -> 10
    QuestDifficulty.MEDIUM -> 25
    QuestDifficulty.HARD -> 60
    QuestDifficulty.EPIC -> 150
}
