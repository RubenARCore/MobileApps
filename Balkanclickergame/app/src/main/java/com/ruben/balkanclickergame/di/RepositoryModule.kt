package com.ruben.balkanclickergame.di

import com.ruben.balkanclickergame.data.repository.GameRepositoryImpl
import com.ruben.balkanclickergame.data.repository.UpgradeRepositoryImpl
import com.ruben.balkanclickergame.domain.repository.GameRepository
import com.ruben.balkanclickergame.domain.repository.UpgradeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGameRepository(
        gameRepositoryImpl: GameRepositoryImpl
    ): GameRepository

    @Binds
    @Singleton
    abstract fun bindUpgradeRepository(
        upgradeRepositoryImpl: UpgradeRepositoryImpl
    ): UpgradeRepository
}
