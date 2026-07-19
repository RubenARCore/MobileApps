package com.ruben.balkanclickergame.domain.model

data class GameState(
    val score: Long = 0,
    val clickPower: Long = 1,
    val upgradesOwned: Map<String, Int> = emptyMap(), // id -> level
    val totalPassiveIncome: Long = 0,
    val lifetimeCoins: Long = 0,
    val lifetimeClicks: Long = 0,
    val unlockedAchievementIds: Set<String> = emptySet(),
    val prestigePoints: Long = 0,
    val prestigeMultiplier: Double = 1.0
)
