package com.ruben.balkanclickergame.domain.usecase

import com.ruben.balkanclickergame.domain.repository.UpgradeRepository
import javax.inject.Inject

class CalculateClickPowerUseCase @Inject constructor(
    private val upgradeRepository: UpgradeRepository
) {
    operator fun invoke(upgradesOwned: Map<String, Int>, prestigeMultiplier: Double = 1.0): Long {
        var totalPower = 1L // Base click power
        upgradesOwned.forEach { (id, level) ->
            val upgrade = upgradeRepository.getUpgradeById(id)
            if (upgrade != null) {
                totalPower += upgrade.clickPowerPerLevel * level
            }
        }
        return (totalPower * prestigeMultiplier).toLong()
    }
}
