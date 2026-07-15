package com.ruben.randomquest.data

import com.ruben.randomquest.model.Quest
import com.ruben.randomquest.model.QuestCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar
import java.util.concurrent.TimeUnit

class QuestRepositoryTest {

    private val fakeDao = object : QuestDao {
        var quests = mutableListOf<Quest>()
        var completionDates = mutableListOf<Long>()

        override suspend fun getRandomQuest(language: String, category: QuestCategory?): Quest? = null
        override fun getCompletedQuests(): Flow<List<Quest>> = flowOf(emptyList())
        override fun getCompletedQuestsCount(): Flow<Int> = flowOf(0)
        override suspend fun updateQuest(quest: Quest) {}
        override suspend fun insertQuests(quests: List<Quest>) {}
        override fun getAllQuests(): Flow<List<Quest>> = flowOf(emptyList())
        override suspend fun getQuestsCount(): Int = 0
        override suspend fun getCompletionDates(): List<Long> = completionDates
    }

    private val repository = QuestRepository(fakeDao)

    @Test
    fun `getStreak returns correct streak for consecutive days`() = runBlocking {
        val calendar = Calendar.getInstance()
        val today = calendar.timeInMillis
        
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val yesterday = calendar.timeInMillis
        
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val twoDaysAgo = calendar.timeInMillis

        fakeDao.completionDates = mutableListOf(today, yesterday, twoDaysAgo)
        
        assertEquals(3, repository.getStreak())
    }

    @Test
    fun `getStreak returns 0 if no completions recently`() = runBlocking {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -3)
        val threeDaysAgo = calendar.timeInMillis

        fakeDao.completionDates = mutableListOf(threeDaysAgo)
        
        assertEquals(0, repository.getStreak())
    }
}
