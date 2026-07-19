package com.ruben.balkanclickergame.domain.usecase

import com.ruben.balkanclickergame.domain.model.GameState
import com.ruben.balkanclickergame.domain.repository.GameRepository
import com.ruben.balkanclickergame.presentation.manager.AchievementNotificationManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ProcessClickUseCaseTest {

    private class FakeGameRepository : GameRepository {
        val state = MutableStateFlow(GameState())
        override fun getGameState(): Flow<GameState> = state
        override suspend fun updateGameState(state: GameState) {
            this.state.value = state
        }
    }

    @Test
    fun `when click processed, score increases by clickPower and lifetime stats are updated`() = runTest {
        val repository = FakeGameRepository()
        repository.updateGameState(GameState(score = 10, clickPower = 5, lifetimeCoins = 10, lifetimeClicks = 2))
        val useCase = ProcessClickUseCase(
            repository,
            CheckAchievementsUseCase(),
            AchievementNotificationManager()
        )

        useCase()

        assertEquals(15, repository.state.value.score)
        assertEquals(15, repository.state.value.lifetimeCoins)
        assertEquals(3, repository.state.value.lifetimeClicks)
    }
}
