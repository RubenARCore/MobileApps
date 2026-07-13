package com.ruben.randomquest.data

import androidx.room.*
import com.ruben.randomquest.model.EnergyLevel
import com.ruben.randomquest.model.Quest
import com.ruben.randomquest.model.QuestCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestDao {
    @Query("SELECT * FROM quests WHERE isCompleted = 0 AND (:category IS NULL OR category = :category) AND (:energyLevel IS NULL OR energyLevel = :energyLevel) ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomQuest(category: QuestCategory?, energyLevel: EnergyLevel?): Quest?

    @Query("SELECT * FROM quests WHERE isCompleted = 1 ORDER BY completedAt DESC")
    fun getCompletedQuests(): Flow<List<Quest>>

    @Query("SELECT COUNT(*) FROM quests WHERE isCompleted = 1")
    fun getCompletedQuestsCount(): Flow<Int>

    @Update
    suspend fun updateQuest(quest: Quest)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuests(quests: List<Quest>)

    @Query("SELECT * FROM quests")
    fun getAllQuests(): Flow<List<Quest>>

    @Query("SELECT COUNT(*) FROM quests")
    suspend fun getQuestsCount(): Int

    @Query("SELECT completedAt FROM quests WHERE isCompleted = 1 ORDER BY completedAt DESC")
    suspend fun getCompletionDates(): List<Long>
}
