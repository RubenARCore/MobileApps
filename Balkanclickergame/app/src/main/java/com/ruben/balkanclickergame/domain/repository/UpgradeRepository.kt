package com.ruben.balkanclickergame.domain.repository

import com.ruben.balkanclickergame.domain.model.Upgrade

interface UpgradeRepository {
    fun getAvailableUpgrades(): List<Upgrade>
    fun getUpgradeById(id: String): Upgrade?
}
