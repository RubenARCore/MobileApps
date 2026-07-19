package com.ruben.balkanclickergame.domain.usecase

import com.ruben.balkanclickergame.domain.model.GameState
import com.ruben.balkanclickergame.domain.repository.GameRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class EmigrateUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {
    suspend operator fun invoke() {
        val currentState = gameRepository.getGameState().first()
        
        // Calculate new prestige points
        // Formula: 1 point for every 1,000,000 lifetime coins, or something similar
        // For simplicity: points = lifetimeCoins / 100000
        val newPoints = (currentState.lifetimeCoins / 100000) - currentState.prestigePoints
        if (newPoints <= 0) return // Must gain at least 1 point to emigrate
        
        val updatedPrestigePoints = currentState.prestigePoints + newPoints
        val updatedPrestigeMultiplier = 1.0 + (updatedPrestigePoints * 0.1) // 10% per point
        
        val newState = GameState(
            score = 0,
            clickPower = 1,
            upgradesOwned = emptyMap(),
            totalPassiveIncome = 0,
            lifetimeCoins = currentState.lifetimeCoins,
            lifetimeClicks = currentState.lifetimeClicks,
            unlockedAchievementIds = currentState.unlockedAchievementIds,
            prestigePoints = updatedPrestigePoints,
            prestigeMultiplier = updatedPrestigeMultiplier
        )
        
        gameRepository.updateGameState(newState)
    }
}
