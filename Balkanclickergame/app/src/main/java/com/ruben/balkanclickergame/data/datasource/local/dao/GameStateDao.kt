package com.ruben.balkanclickergame.data.datasource.local.dao

import androidx.room.*
import com.ruben.balkanclickergame.data.datasource.local.entity.GameStateEntity
import com.ruben.balkanclickergame.data.datasource.local.entity.OwnedUpgradeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameStateDao {
    @Query("SELECT * FROM game_state WHERE id = 0")
    fun getGameState(): Flow<GameStateEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameState(gameState: GameStateEntity)

    @Query("SELECT * FROM owned_upgrades")
    fun getOwnedUpgrades(): Flow<List<OwnedUpgradeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOwnedUpgrade(upgrade: OwnedUpgradeEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOwnedUpgrades(upgrades: List<OwnedUpgradeEntity>)

    @Query("DELETE FROM owned_upgrades")
    suspend fun deleteAllUpgrades()
    
    @Transaction
    suspend fun resetProgress() {
        deleteAllUpgrades()
        // Reset game state will be handled by insertGameState with default values
    }
}
