package com.ruben.balkanclickergame.presentation.manager

import com.ruben.balkanclickergame.domain.model.Achievement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AchievementNotificationManager @Inject constructor() {
    private val _currentAchievement = MutableStateFlow<Achievement?>(null)
    val currentAchievement = _currentAchievement.asStateFlow()

    private val queue = mutableListOf<Achievement>()

    fun show(achievement: Achievement) {
        if (_currentAchievement.value == null) {
            _currentAchievement.value = achievement
        } else {
            queue.add(achievement)
        }
    }

    fun dismiss() {
        if (queue.isNotEmpty()) {
            _currentAchievement.value = queue.removeAt(0)
        } else {
            _currentAchievement.value = null
        }
    }
}
