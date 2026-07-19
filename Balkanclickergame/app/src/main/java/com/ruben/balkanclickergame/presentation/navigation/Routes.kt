package com.ruben.balkanclickergame.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Routes : NavKey {
    @Serializable
    data object Dashboard : Routes

    @Serializable
    data object Shop : Routes
}
