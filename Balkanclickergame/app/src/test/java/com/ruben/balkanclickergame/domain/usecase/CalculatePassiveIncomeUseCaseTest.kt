package com.ruben.balkanclickergame.domain.usecase

import com.ruben.balkanclickergame.R
import com.ruben.balkanclickergame.domain.model.Upgrade
import com.ruben.balkanclickergame.domain.repository.UpgradeRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculatePassiveIncomeUseCaseTest {

    private val upgradeRepository: UpgradeRepository = mockk()
    private val useCase = CalculatePassiveIncomeUseCase(upgradeRepository)

    @Test
    fun `invoke with no upgrades returns 0`() {
        val result = useCase(emptyMap())
        assertEquals(0L, result)
    }

    @Test
    fun `invoke with upgrades calculates correct total`() {
        val upgrade1 = Upgrade("u1", R.string.app_name, R.string.app_name, 10, passiveIncomePerLevel = 5)
        val upgrade2 = Upgrade("u2", R.string.app_name, R.string.app_name, 20, passiveIncomePerLevel = 10)
        
        every { upgradeRepository.getUpgradeById("u1") } returns upgrade1
        every { upgradeRepository.getUpgradeById("u2") } returns upgrade2

        val upgradesOwned = mapOf("u1" to 2, "u2" to 1)
        // 5 * 2 + 10 * 1 = 20
        
        val result = useCase(upgradesOwned)
        assertEquals(20L, result)
    }
}
