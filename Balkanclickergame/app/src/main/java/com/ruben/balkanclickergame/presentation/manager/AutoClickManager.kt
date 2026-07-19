package com.ruben.balkanclickergame.presentation.manager

import com.ruben.balkanclickergame.domain.repository.GameRepository
import com.ruben.balkanclickergame.domain.usecase.ProcessClickUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AutoClickManager @Inject constructor(
    private val gameRepository: GameRepository,
    private val processClickUseCase: ProcessClickUseCase
) {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var job: Job? = null

    fun start() {
        if (job != null) return
        job = scope.launch {
            while (isActive) {
                val state = gameRepository.getGameState().first()
                val itNephewLevel = state.upgradesOwned["it_nephew"] ?: 0
                if (itNephewLevel > 0) {
                    // Clicks 2 times per second per level
                    val delayMillis = 1000L / (itNephewLevel * 2)
                    processClickUseCase()
                    delay(delayMillis)
                } else {
                    delay(1000) // Check again in a second
                }
            }
        }
    }

    fun stop() {
        job?.cancel()
        job = null
    }
}
