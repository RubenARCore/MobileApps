package com.ruben.balkanclickergame.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_state")
data class GameStateEntity(
    @PrimaryKey val id: Int = 0, // Single row for game state
    val score: Long,
    val clickPower: Long,
    val totalPassiveIncome: Long,
    val lifetimeCoins: Long,
    val lifetimeClicks: Long,
    val unlockedAchievementIdsJson: String, // Store as JSON string or use TypeConverter
    val prestigePoints: Long,
    val prestigeMultiplier: Double
)
