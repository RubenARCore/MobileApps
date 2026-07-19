package com.ruben.balkanclickergame.domain.usecase

import com.ruben.balkanclickergame.domain.model.GameState
import com.ruben.balkanclickergame.domain.repository.GameRepository
import com.ruben.balkanclickergame.presentation.manager.AchievementNotificationManager
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ProcessPassiveIncomeUseCaseTest {

    private class FakeGameRepository : GameRepository {
        val state = MutableStateFlow(GameState())
        override fun getGameState(): Flow<GameState> = state
        override suspend fun updateGameState(state: GameState) {
            this.state.value = state
        }
    }

    @Test
    fun `when passive income processed, score and lifetimeCoins increase`() = runTest {
        val repository = FakeGameRepository()
        repository.updateGameState(GameState(score = 10, totalPassiveIncome = 5, lifetimeCoins = 10))
        val useCase = ProcessPassiveIncomeUseCase(
            repository,
            CheckAchievementsUseCase(),
            AchievementNotificationManager(mockk(relaxed = true))
        )

        useCase()

        assertEquals(15, repository.state.value.score)
        assertEquals(15, repository.state.value.lifetimeCoins)
    }

    @Test
    fun `when passive income is zero, nothing changes`() = runTest {
        val repository = FakeGameRepository()
        repository.updateGameState(GameState(score = 10, totalPassiveIncome = 0, lifetimeCoins = 10))
        val useCase = ProcessPassiveIncomeUseCase(
            repository,
            CheckAchievementsUseCase(),
            AchievementNotificationManager(mockk(relaxed = true))
        )

        useCase()

        assertEquals(10, repository.state.value.score)
        assertEquals(10, repository.state.value.lifetimeCoins)
    }
}
