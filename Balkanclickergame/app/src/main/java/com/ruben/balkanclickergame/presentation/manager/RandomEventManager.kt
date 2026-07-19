package com.ruben.balkanclickergame.presentation.manager

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

sealed class GameEvent {
    object None : GameEvent()
    data class Wedding(val multiplier: Double = 2.0, val duration: Long = 30000) : GameEvent()
    data class Inflation(val penalty: Double = 0.5, val duration: Long = 20000) : GameEvent()
}

@Singleton
class RandomEventManager @Inject constructor(
    private val feedbackManager: FeedbackManager
) {
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val _currentEvent = mutableStateOf<GameEvent>(GameEvent.None)
    val currentEvent: State<GameEvent> = _currentEvent

    fun start() {
        scope.launch {
            while (isActive) {
                delay(Random.nextLong(60000, 180000)) // Every 1-3 minutes
                triggerRandomEvent()
            }
        }
    }

    private suspend fun triggerRandomEvent() {
        val event = if (Random.nextBoolean()) {
            GameEvent.Wedding()
        } else {
            GameEvent.Inflation()
        }
        
        feedbackManager.vibrateSuccess() // Signal event start
        _currentEvent.value = event
        
        val duration = when (event) {
            is GameEvent.Wedding -> event.duration
            is GameEvent.Inflation -> event.duration
            else -> 0L
        }
        
        delay(duration)
        _currentEvent.value = GameEvent.None
    }
}
