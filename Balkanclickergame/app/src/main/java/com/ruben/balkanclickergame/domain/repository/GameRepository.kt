package com.ruben.balkanclickergame.domain.repository

import com.ruben.balkanclickergame.domain.model.GameState
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun getGameState(): Flow<GameState>
    suspend fun updateGameState(state: GameState)
}
