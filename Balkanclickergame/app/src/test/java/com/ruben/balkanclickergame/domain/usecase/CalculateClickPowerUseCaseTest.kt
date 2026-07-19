package com.ruben.balkanclickergame.domain.usecase

import com.ruben.balkanclickergame.R
import com.ruben.balkanclickergame.domain.model.Upgrade
import com.ruben.balkanclickergame.domain.repository.UpgradeRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculateClickPowerUseCaseTest {

    private val upgradeRepository: UpgradeRepository = mockk()
    private val useCase = CalculateClickPowerUseCase(upgradeRepository)

    @Test
    fun `invoke with no upgrades returns base power 1`() {
        val result = useCase(emptyMap())
        assertEquals(1L, result)
    }

    @Test
    fun `invoke with upgrades calculates correct total click power`() {
        val upgrade1 = Upgrade("u1", R.string.app_name, R.string.app_name, 10, clickPowerPerLevel = 2)
        val upgrade2 = Upgrade("u2", R.string.app_name, R.string.app_name, 20, clickPowerPerLevel = 5)
        
        every { upgradeRepository.getUpgradeById("u1") } returns upgrade1
        every { upgradeRepository.getUpgradeById("u2") } returns upgrade2

        val upgradesOwned = mapOf("u1" to 3, "u2" to 1)
        // 1 (base) + 2 * 3 + 5 * 1 = 1 + 6 + 5 = 12
        
        val result = useCase(upgradesOwned)
        assertEquals(12L, result)
    }
}
