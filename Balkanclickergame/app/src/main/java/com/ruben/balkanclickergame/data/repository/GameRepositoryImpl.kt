package com.ruben.balkanclickergame.data.repository

import com.ruben.balkanclickergame.data.datasource.local.dao.GameStateDao
import com.ruben.balkanclickergame.data.mapper.toDomain
import com.ruben.balkanclickergame.data.mapper.toEntity
import com.ruben.balkanclickergame.data.mapper.toMap
import com.ruben.balkanclickergame.data.mapper.toOwnedUpgradeEntities
import com.ruben.balkanclickergame.domain.model.GameState
import com.ruben.balkanclickergame.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepositoryImpl @Inject constructor(
    private val gameStateDao: GameStateDao
) : GameRepository {

    override fun getGameState(): Flow<GameState> {
        return combine(
            gameStateDao.getGameState(),
            gameStateDao.getOwnedUpgrades()
        ) { entity, upgrades ->
            entity?.toDomain(upgrades.toMap()) ?: GameState()
        }
    }

    override suspend fun updateGameState(state: GameState) {
        gameStateDao.insertGameState(state.toEntity())
        gameStateDao.insertOwnedUpgrades(state.upgradesOwned.toOwnedUpgradeEntities())
    }
}
