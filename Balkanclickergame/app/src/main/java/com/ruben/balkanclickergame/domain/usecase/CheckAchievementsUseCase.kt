package com.ruben.balkanclickergame.domain.usecase

import com.ruben.balkanclickergame.domain.model.Achievement
import com.ruben.balkanclickergame.domain.model.Achievements
import com.ruben.balkanclickergame.domain.model.GameState
import javax.inject.Inject

class CheckAchievementsUseCase @Inject constructor() {
    operator fun invoke(gameState: GameState): List<Achievement> {
        return Achievements.ALL.filter { achievement ->
            !gameState.unlockedAchievementIds.contains(achievement.id) && achievement.criteria(gameState)
        }
    }
}
