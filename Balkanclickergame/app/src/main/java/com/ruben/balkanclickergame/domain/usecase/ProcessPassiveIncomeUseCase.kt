package com.ruben.balkanclickergame.domain.usecase

import com.ruben.balkanclickergame.domain.repository.GameRepository
import com.ruben.balkanclickergame.presentation.manager.AchievementNotificationManager
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ProcessPassiveIncomeUseCase @Inject constructor(
    private val repository: GameRepository,
    private val checkAchievementsUseCase: CheckAchievementsUseCase,
    private val achievementNotificationManager: AchievementNotificationManager
) {
    suspend operator fun invoke(eventMultiplier: Double = 1.0) {
        val currentState = repository.getGameState().first()
        val income = (currentState.totalPassiveIncome * eventMultiplier * currentState.prestigeMultiplier).toLong()
        if (income > 0) {
            var newState = currentState.copy(
                score = currentState.score + income,
                lifetimeCoins = currentState.lifetimeCoins + income
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
}
