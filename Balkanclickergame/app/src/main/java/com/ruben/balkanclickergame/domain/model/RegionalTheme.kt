package com.ruben.balkanclickergame.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector

data class RegionalTheme(
    val id: String,
    val currencySymbol: String,
    val currencyIcon: ImageVector,
    val threshold: Long
)

object RegionalThemes {
    val BGN = RegionalTheme("bgn", "лв.", Icons.Rounded.Savings, 0)
    val RSD = RegionalTheme("rsd", "дин.", Icons.Rounded.AccountBalance, 10000)
    val TRY = RegionalTheme("try", "₺", Icons.Rounded.CurrencyLira, 50000)
    val EUR = RegionalTheme("eur", "€", Icons.Rounded.Euro, 200000)

    val all = listOf(BGN, RSD, TRY, EUR)

    fun getThemeForLifetimeCoins(lifetimeCoins: Long): RegionalTheme {
        return all.reversed().find { lifetimeCoins >= it.threshold } ?: BGN
    }
}
