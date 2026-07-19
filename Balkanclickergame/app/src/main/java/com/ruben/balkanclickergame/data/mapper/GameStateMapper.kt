package com.ruben.balkanclickergame.data.mapper

import com.ruben.balkanclickergame.data.datasource.local.entity.GameStateEntity
import com.ruben.balkanclickergame.data.datasource.local.entity.OwnedUpgradeEntity
import com.ruben.balkanclickergame.domain.model.GameState
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun GameState.toEntity(): GameStateEntity {
    return GameStateEntity(
        score = score,
        clickPower = clickPower,
        totalPassiveIncome = totalPassiveIncome,
        lifetimeCoins = lifetimeCoins,
        lifetimeClicks = lifetimeClicks,
        unlockedAchievementIdsJson = Json.encodeToString(unlockedAchievementIds),
        prestigePoints = prestigePoints,
        prestigeMultiplier = prestigeMultiplier
    )
}

fun GameStateEntity.toDomain(upgrades: Map<String, Int>): GameState {
    return GameState(
        score = score,
        clickPower = clickPower,
        upgradesOwned = upgrades,
        totalPassiveIncome = totalPassiveIncome,
        lifetimeCoins = lifetimeCoins,
        lifetimeClicks = lifetimeClicks,
        unlockedAchievementIds = Json.decodeFromString(unlockedAchievementIdsJson),
        prestigePoints = prestigePoints,
        prestigeMultiplier = prestigeMultiplier
    )
}

fun Map<String, Int>.toOwnedUpgradeEntities(): List<OwnedUpgradeEntity> {
    return map { (id, level) ->
        OwnedUpgradeEntity(upgradeId = id, level = level)
    }
}

fun List<OwnedUpgradeEntity>.toMap(): Map<String, Int> {
    return associate { it.upgradeId to it.level }
}
