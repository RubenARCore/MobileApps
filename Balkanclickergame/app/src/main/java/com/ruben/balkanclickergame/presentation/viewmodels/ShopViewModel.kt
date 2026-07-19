package com.ruben.balkanclickergame.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruben.balkanclickergame.domain.model.Upgrade
import com.ruben.balkanclickergame.domain.repository.UpgradeRepository
import com.ruben.balkanclickergame.domain.usecase.GetGameStateUseCase
import com.ruben.balkanclickergame.domain.usecase.PurchaseUpgradeUseCase
import com.ruben.balkanclickergame.presentation.manager.FeedbackManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UpgradeUiModel(
    val upgrade: Upgrade,
    val currentLevel: Int,
    val currentCost: Long,
    val canAfford: Boolean
)

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val upgradeRepository: UpgradeRepository,
    private val getGameStateUseCase: GetGameStateUseCase,
    private val purchaseUpgradeUseCase: PurchaseUpgradeUseCase,
    private val feedbackManager: FeedbackManager
) : ViewModel() {

    val shopItems: StateFlow<List<UpgradeUiModel>> = getGameStateUseCase()
        .map { state ->
            val municipalLevel = state.upgradesOwned["municipal_connections"] ?: 0
            val discountMultiplier = Math.max(0.5, 1.0 - (municipalLevel * 0.05))

            upgradeRepository.getAvailableUpgrades().map { upgrade ->
                val level = state.upgradesOwned[upgrade.id] ?: 0
                val cost = upgrade.calculateCost(level, discountMultiplier)
                UpgradeUiModel(
                    upgrade = upgrade,
                    currentLevel = level,
                    currentCost = cost,
                    canAfford = state.score >= cost
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun purchaseUpgrade(upgradeId: String) {
        viewModelScope.launch {
            val result = purchaseUpgradeUseCase(upgradeId)
            if (result.isSuccess) {
                feedbackManager.vibrateSuccess()
                feedbackManager.playPurchaseSound()
            }
        }
    }
}
