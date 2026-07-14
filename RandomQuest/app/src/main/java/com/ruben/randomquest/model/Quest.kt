package com.ruben.randomquest.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
enum class QuestCategory {
    SOCIAL, FITNESS, CREATIVE, MINDFUL
}

@Serializable
enum class EnergyLevel {
    LOW, MEDIUM, HIGH
}

@Entity(tableName = "quests")
@Serializable
data class Quest(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val category: QuestCategory,
    val energyLevel: EnergyLevel,
    val language: String = "en",
    val isCompleted: Boolean = false,
    val completedAt: Long? = null
)
