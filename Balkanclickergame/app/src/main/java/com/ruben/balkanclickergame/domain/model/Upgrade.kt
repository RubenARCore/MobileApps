package com.ruben.balkanclickergame.domain.model

import androidx.annotation.StringRes

data class Upgrade(
    val id: String,
    @StringRes val nameResId: Int,
    @StringRes val descriptionResId: Int,
    val baseCost: Long,
    val passiveIncomePerLevel: Long = 0,
    val clickPowerPerLevel: Long = 0
) {
    fun calculateCost(level: Int, discountMultiplier: Double = 1.0): Long {
        val baseCalculated = (baseCost * Math.pow(1.15, level.toDouble())).toLong()
        return (baseCalculated * discountMultiplier).toLong()
    }
}
