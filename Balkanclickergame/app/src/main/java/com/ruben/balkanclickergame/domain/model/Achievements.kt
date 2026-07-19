package com.ruben.balkanclickergame.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.AdsClick
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.TrendingUp

object Achievements {
    val ALL = listOf(
        Achievement(
            id = "first_click",
            title = "Balkan Beginner",
            description = "You clicked the Rakija! Your first step into the Balkan economy.",
            icon = Icons.Rounded.AdsClick,
            criteria = { it.lifetimeClicks >= 1 }
        ),
        Achievement(
            id = "thousand_coins",
            title = "Hard Worker",
            description = "You earned 1,000 coins. You can finally afford a decent coffee.",
            icon = Icons.Rounded.MonetizationOn,
            criteria = { it.lifetimeCoins >= 1000 }
        ),
        Achievement(
            id = "first_upgrade",
            title = "Investor",
            description = "Bought your first upgrade. The Rakija Distiller is proud.",
            icon = Icons.Rounded.TrendingUp,
            criteria = { it.upgradesOwned.values.sum() >= 1 }
        ),
        Achievement(
            id = "millionaire",
            title = "Balkan Oligarch",
            description = "Earned 1,000,000 coins lifetime. You're basically running the village.",
            icon = Icons.Rounded.AccountBalance,
            criteria = { it.lifetimeCoins >= 1000000 }
        ),
        Achievement(
            id = "click_master",
            title = "Finger of Steel",
            description = "Clicked 10,000 times. Your finger must be very strong.",
            icon = Icons.Rounded.Star,
            criteria = { it.lifetimeClicks >= 10000 }
        )
    )
}
