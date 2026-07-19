package com.ruben.balkanclickergame.domain.usecase

import com.ruben.balkanclickergame.domain.model.GameState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CheckAchievementsUseCaseTest {

    private val checkAchievementsUseCase = CheckAchievementsUseCase()

    @Test
    fun `first click achievement is unlocked on first click`() {
        val gameState = GameState(lifetimeClicks = 1)
        val newAchievements = checkAchievementsUseCase(gameState)
        
        assertEquals(1, newAchievements.size)
        assertEquals("first_click", newAchievements[0].id)
    }

    @Test
    fun `already unlocked achievements are not returned`() {
        val gameState = GameState(
            lifetimeClicks = 1,
            unlockedAchievementIds = setOf("first_click")
        )
        val newAchievements = checkAchievementsUseCase(gameState)
        
        assertTrue(newAchievements.isEmpty())
    }

    @Test
    fun `multiple achievements can be unlocked at once`() {
        val gameState = GameState(
            lifetimeClicks = 1,
            lifetimeCoins = 1000
        )
        val newAchievements = checkAchievementsUseCase(gameState)
        
        assertEquals(2, newAchievements.size)
        val ids = newAchievements.map { it.id }
        assertTrue(ids.contains("first_click"))
        assertTrue(ids.contains("thousand_coins"))
    }
}
