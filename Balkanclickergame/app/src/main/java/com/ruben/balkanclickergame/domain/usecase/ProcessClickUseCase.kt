package com.ruben.balkanclickergame.domain.usecase

import com.ruben.balkanclickergame.domain.repository.GameRepository
import com.ruben.balkanclickergame.presentation.manager.AchievementNotificationManager
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ProcessClickUseCase @Inject constructor(
    private val repository: GameRepository,
    private val checkAchievementsUseCase: CheckAchievementsUseCase,
    private val achievementNotificationManager: AchievementNotificationManager
) {
    suspend operator fun invoke(eventMultiplier: Double = 1.0) {
        val currentState = repository.getGameState().first()
        val clickPower = (currentState.clickPower * eventMultiplier * currentState.prestigeMultiplier).toLong()
        var newState = currentState.copy(
            score = currentState.score + clickPower,
            lifetimeCoins = currentState.lifetimeCoins + clickPower,
            lifetimeClicks = currentState.lifetimeClicks + 1
        )
        
        val newAchievements = checkAchievementsUseCase(newState)
        if (newAchievements.isNotEmpty()) {
            newState = newState.copy(
                unlockedAchievementIds = newState.unlockedAchievementIds + newAchievements.map { it.id }
            )
            newAchievements.forEach { achievementNotificationManager.show(it) }
        }
        
        repository.updateGameState(newState)
    }
}
