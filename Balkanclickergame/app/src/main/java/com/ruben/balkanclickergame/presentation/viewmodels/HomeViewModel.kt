package com.ruben.balkanclickergame.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruben.balkanclickergame.domain.model.GameState
import com.ruben.balkanclickergame.domain.usecase.EmigrateUseCase
import com.ruben.balkanclickergame.domain.usecase.GetGameStateUseCase
import com.ruben.balkanclickergame.domain.usecase.ProcessClickUseCase
import com.ruben.balkanclickergame.domain.usecase.ProcessPassiveIncomeUseCase
import com.ruben.balkanclickergame.presentation.manager.AutoClickManager
import com.ruben.balkanclickergame.presentation.manager.FeedbackManager
import com.ruben.balkanclickergame.presentation.manager.GameEvent
import com.ruben.balkanclickergame.presentation.manager.RandomEventManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getGameStateUseCase: GetGameStateUseCase,
    private val processClickUseCase: ProcessClickUseCase,
    private val processPassiveIncomeUseCase: ProcessPassiveIncomeUseCase,
    private val emigrateUseCase: EmigrateUseCase,
    private val autoClickManager: AutoClickManager,
    private val randomEventManager: RandomEventManager,
    private val feedbackManager: FeedbackManager
) : ViewModel() {

    val gameState: StateFlow<GameState> = getGameStateUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GameState()
        )

    val currentEvent = randomEventManager.currentEvent

    init {
        startPassiveIncomeLoop()
        autoClickManager.start()
        randomEventManager.start()
    }

    private fun startPassiveIncomeLoop() {
        viewModelScope.launch {
            while (isActive) {
                delay(1000)
                val multiplier = when (val event = currentEvent.value) {
                    is GameEvent.Wedding -> event.multiplier
                    is GameEvent.Inflation -> event.penalty
                    else -> 1.0
                }
                processPassiveIncomeUseCase(multiplier)
            }
        }
    }

    fun onCoinClicked() {
        feedbackManager.vibrateClick()
        feedbackManager.playClickSound()
        viewModelScope.launch {
            val multiplier = when (val event = currentEvent.value) {
                is GameEvent.Wedding -> event.multiplier
                is GameEvent.Inflation -> event.penalty
                else -> 1.0
            }
            processClickUseCase(multiplier)
        }
    }

    fun onEmigrateClicked() {
        viewModelScope.launch {
            emigrateUseCase()
        }
    }
}
