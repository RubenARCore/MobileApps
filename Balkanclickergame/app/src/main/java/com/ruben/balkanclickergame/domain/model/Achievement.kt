package com.ruben.balkanclickergame.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val criteria: (GameState) -> Boolean
)
