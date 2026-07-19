package com.ruben.balkanclickergame.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "owned_upgrades")
data class OwnedUpgradeEntity(
    @PrimaryKey val upgradeId: String,
    val level: Int
)
