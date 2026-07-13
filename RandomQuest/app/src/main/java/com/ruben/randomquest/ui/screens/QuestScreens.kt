package com.ruben.randomquest.ui.screens

import kotlinx.serialization.Serializable

@Serializable
sealed interface QuestNavKey {
    @Serializable
    data object Generator : QuestNavKey
    
    @Serializable
    data object Profile : QuestNavKey
}
