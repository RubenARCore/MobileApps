package com.ruben.balkanclickergame.domain.usecase

import com.ruben.balkanclickergame.domain.model.GameState
import com.ruben.balkanclickergame.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGameStateUseCase @Inject constructor(
    private val repository: GameRepository
) {
    operator fun invoke(): Flow<GameState> {
        return repository.getGameState()
    }
}
