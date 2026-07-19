package com.ruben.balkanclickergame.data.repository

import com.ruben.balkanclickergame.R
import com.ruben.balkanclickergame.domain.model.Upgrade
import com.ruben.balkanclickergame.domain.repository.UpgradeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpgradeRepositoryImpl @Inject constructor() : UpgradeRepository {
    private val upgrades = listOf(
        Upgrade(
            id = "rakija_still",
            nameResId = R.string.upgrade_rakija_name,
            descriptionResId = R.string.upgrade_rakija_desc,
            baseCost = 15,
            passiveIncomePerLevel = 1
        ),
        Upgrade(
            id = "baba_advice",
            nameResId = R.string.upgrade_baba_name,
            descriptionResId = R.string.upgrade_baba_desc,
            baseCost = 100,
            clickPowerPerLevel = 2
        ),
        Upgrade(
            id = "coffee_shop",
            nameResId = R.string.upgrade_kafana_name,
            descriptionResId = R.string.upgrade_kafana_desc,
            baseCost = 500,
            passiveIncomePerLevel = 8
        ),
        Upgrade(
            id = "yugo_service",
            nameResId = R.string.upgrade_yugo_name,
            descriptionResId = R.string.upgrade_yugo_desc,
            baseCost = 2500,
            passiveIncomePerLevel = 45
        ),
        Upgrade(
            id = "eurovision_win",
            nameResId = R.string.upgrade_eurovision_name,
            descriptionResId = R.string.upgrade_eurovision_desc,
            baseCost = 10000,
            passiveIncomePerLevel = 150,
            clickPowerPerLevel = 10
        ),
        Upgrade(
            id = "municipal_connections",
            nameResId = R.string.upgrade_municipal_connections_name,
            descriptionResId = R.string.upgrade_municipal_connections_desc,
            baseCost = 250,
        ),
        Upgrade(
            id = "it_nephew",
            nameResId = R.string.upgrade_it_nephew_name,
            descriptionResId = R.string.upgrade_it_nephew_desc,
            baseCost = 1000,
        )
    )

    override fun getAvailableUpgrades(): List<Upgrade> = upgrades

    override fun getUpgradeById(id: String): Upgrade? = upgrades.find { it.id == id }
}
