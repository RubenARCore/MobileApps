package com.ruben.balkanclickergame.di

import android.content.Context
import androidx.room.Room
import com.ruben.balkanclickergame.data.datasource.local.GameDatabase
import com.ruben.balkanclickergame.data.datasource.local.dao.GameStateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideGameDatabase(@ApplicationContext context: Context): GameDatabase {
        return Room.databaseBuilder(
            context,
            GameDatabase::class.java,
            "balkan_clicker_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideGameStateDao(database: GameDatabase): GameStateDao {
        return database.gameStateDao()
    }
}
