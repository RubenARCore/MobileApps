package com.ruben.balkanclickergame.domain.usecase

import com.ruben.balkanclickergame.domain.repository.GameRepository
import com.ruben.balkanclickergame.domain.repository.UpgradeRepository
import com.ruben.balkanclickergame.presentation.manager.AchievementNotificationManager
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PurchaseUpgradeUseCase @Inject constructor(
    private val gameRepository: GameRepository,
    private val upgradeRepository: UpgradeRepository,
    private val calculatePassiveIncomeUseCase: CalculatePassiveIncomeUseCase,
    private val calculateClickPowerUseCase: CalculateClickPowerUseCase,
    private val checkAchievementsUseCase: CheckAchievementsUseCase,
    private val achievementNotificationManager: AchievementNotificationManager
) {
    suspend operator fun invoke(upgradeId: String): Result<Unit> {
        val gameState = gameRepository.getGameState().first()
        val upgrade = upgradeRepository.getUpgradeById(upgradeId) ?: return Result.failure(Exception("Upgrade not found"))
        
        val currentLevel = gameState.upgradesOwned[upgradeId] ?: 0
        
        // Calculate discount from "Municipal Connections"
        val municipalLevel = gameState.upgradesOwned["municipal_connections"] ?: 0
        val discountMultiplier = Math.max(0.5, 1.0 - (municipalLevel * 0.05)) // Max 50% discount
        
        val cost = upgrade.calculateCost(currentLevel, discountMultiplier)
        
        if (gameState.score < cost) {
            return Result.failure(Exception("Not enough score"))
        }
        
        val newUpgradesOwned = gameState.upgradesOwned.toMutableMap().apply {
            put(upgradeId, currentLevel + 1)
        }
        
        val newPassiveIncome = calculatePassiveIncomeUseCase(newUpgradesOwned, gameState.prestigeMultiplier)
        val newClickPower = calculateClickPowerUseCase(newUpgradesOwned, gameState.prestigeMultiplier)
        
        var newState = gameState.copy(
            score = gameState.score - cost,
            upgradesOwned = newUpgradesOwned,
            totalPassiveIncome = newPassiveIncome,
            clickPower = newClickPower
        )

        val newAchievements = checkAchievementsUseCase(newState)
        if (newAchievements.isNotEmpty()) {
            newState = newState.copy(
                unlockedAchievementIds = newState.unlockedAchievementIds + newAchievements.map { it.id }
            )
            newAchievements.forEach { achievementNotificationManager.show(it) }
        }
        
        gameRepository.updateGameState(newState)
        return Result.success(Unit)
    }
}
