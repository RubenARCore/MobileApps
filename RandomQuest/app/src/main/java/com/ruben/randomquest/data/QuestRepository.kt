package com.ruben.randomquest.data

import com.ruben.randomquest.model.Quest
import com.ruben.randomquest.model.QuestCategory
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import java.util.concurrent.TimeUnit

class QuestRepository(private val questDao: QuestDao) {

    fun getAllQuests(): Flow<List<Quest>> = questDao.getAllQuests()
    
    fun getCompletedQuestsCount(): Flow<Int> = questDao.getCompletedQuestsCount()

    suspend fun getRandomQuest(language: String, category: QuestCategory?): Quest? {
        return questDao.getRandomQuest(language, category)
    }

    suspend fun completeQuest(quest: Quest) {
        val completedQuest = quest.copy(
            isCompleted = true,
            completedAt = System.currentTimeMillis()
        )
        questDao.updateQuest(completedQuest)
    }

    suspend fun prepopulateIfNeeded() {
        if (questDao.getQuestsCount() == 0) {
            insertInitialQuests()
        }
    }

    suspend fun getStreak(): Int {
        val dates = questDao.getCompletionDates()
        if (dates.isEmpty()) return 0

        val calendar = Calendar.getInstance()
        val today = calendar.timeInMillis
        
        // Group by day to handle multiple quests on the same day
        val uniqueDays = dates.map {
            calendar.timeInMillis = it
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            calendar.timeInMillis
        }.distinct()

        if (uniqueDays.isEmpty()) return 0

        // Check if the most recent completion was today or yesterday
        calendar.timeInMillis = today
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val todayStart = calendar.timeInMillis
        
        val yesterdayStart = todayStart - TimeUnit.DAYS.toMillis(1)

        if (uniqueDays[0] < yesterdayStart) return 0

        var streak = 1
        for (i in 0 until uniqueDays.size - 1) {
            val current = uniqueDays[i]
            val next = uniqueDays[i + 1]
            
            if (current - next == TimeUnit.DAYS.toMillis(1)) {
                streak++
            } else {
                break
            }
        }

        return streak
    }
    
    suspend fun insertInitialQuests() {
        questDao.insertQuests(QuestDataSource.initialQuests)
    }
}
