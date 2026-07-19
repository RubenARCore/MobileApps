package com.ruben.balkanclickergame.domain.usecase

import com.ruben.balkanclickergame.domain.repository.UpgradeRepository
import javax.inject.Inject

class CalculatePassiveIncomeUseCase @Inject constructor(
    private val upgradeRepository: UpgradeRepository
) {
    operator fun invoke(upgradesOwned: Map<String, Int>, prestigeMultiplier: Double = 1.0): Long {
        var totalIncome = 0L
        upgradesOwned.forEach { (id, level) ->
            val upgrade = upgradeRepository.getUpgradeById(id)
            if (upgrade != null) {
                totalIncome += upgrade.passiveIncomePerLevel * level
            }
        }
        return (totalIncome * prestigeMultiplier).toLong()
    }
}
