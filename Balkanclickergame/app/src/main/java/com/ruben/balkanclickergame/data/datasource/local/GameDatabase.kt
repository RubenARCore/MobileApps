package com.ruben.balkanclickergame.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ruben.balkanclickergame.data.datasource.local.dao.GameDao
import com.ruben.balkanclickergame.data.datasource.local.entity.GameStateEntity
import com.ruben.balkanclickergame.data.datasource.local.entity.OwnedUpgradeEntity

@Database(
    entities = [GameStateEntity::class, OwnedUpgradeEntity::class],
    version = 1,
    exportSchema = false
)
abstract class GameDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
}
