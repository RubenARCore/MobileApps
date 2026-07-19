package com.ruben.balkanclickergame.domain.usecase

import com.ruben.balkanclickergame.domain.model.GameState
import com.ruben.balkanclickergame.R
import com.ruben.balkanclickergame.domain.model.Upgrade
import com.ruben.balkanclickergame.domain.repository.GameRepository
import com.ruben.balkanclickergame.domain.repository.UpgradeRepository
import com.ruben.balkanclickergame.presentation.manager.AchievementNotificationManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PurchaseUpgradeUseCaseTest {

    private val gameRepository: GameRepository = mockk(relaxed = true)
    private val upgradeRepository: UpgradeRepository = mockk()
    private val calculatePassiveIncomeUseCase: CalculatePassiveIncomeUseCase = mockk()
    private val calculateClickPowerUseCase: CalculateClickPowerUseCase = mockk()
    
    private val useCase = PurchaseUpgradeUseCase(
        gameRepository,
        upgradeRepository,
        calculatePassiveIncomeUseCase,
        calculateClickPowerUseCase,
        CheckAchievementsUseCase(),
        AchievementNotificationManager()
    )

    @Test
    fun `purchase upgrade successfully updates state`() = runTest {
        val upgradeId = "rakija"
        val upgrade = Upgrade(upgradeId, R.string.app_name, R.string.app_name, baseCost = 10, passiveIncomePerLevel = 2)
        val initialState = GameState(score = 20, upgradesOwned = emptyMap())
        
        every { upgradeRepository.getUpgradeById(upgradeId) } returns upgrade
        every { gameRepository.getGameState() } returns flowOf(initialState)
        every { calculatePassiveIncomeUseCase(any()) } returns 2L
        every { calculateClickPowerUseCase(any()) } returns 1L
        
        val result = useCase(upgradeId)
        
        assertTrue(result.isSuccess)
        coVerify { 
            gameRepository.updateGameState(match {
                it.score == 10L && it.upgradesOwned[upgradeId] == 1 && it.totalPassiveIncome == 2L
            })
        }
    }

    @Test
    fun `purchase upgrade fails if not enough score`() = runTest {
        val upgradeId = "rakija"
        val upgrade = Upgrade(upgradeId, R.string.app_name, R.string.app_name, baseCost = 100)
        val initialState = GameState(score = 20, upgradesOwned = emptyMap())
        
        every { upgradeRepository.getUpgradeById(upgradeId) } returns upgrade
        every { gameRepository.getGameState() } returns flowOf(initialState)
        
        val result = useCase(upgradeId)
        
        assertTrue(result.isFailure)
        assertEquals("Not enough score", result.exceptionOrNull()?.message)
    }
}
