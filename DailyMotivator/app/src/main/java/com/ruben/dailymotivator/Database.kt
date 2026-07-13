package com.ruben.dailymotivator

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MotivatorDao {
    @Query("SELECT * FROM habits")
    fun getAllHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habits")
    suspend fun getAllHabitsSync(): List<Habit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit)

    @Update
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("SELECT * FROM missions WHERE isCompleted = 0")
    fun getActiveMissions(): Flow<List<DailyMission>>

    @Query("SELECT * FROM missions WHERE isCompleted = 1")
    fun getCompletedMissions(): Flow<List<DailyMission>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMission(mission: DailyMission)

    @Update
    suspend fun updateMission(mission: DailyMission)

    @Delete
    suspend fun deleteMission(mission: DailyMission)

    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<FavoriteItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(item: FavoriteItem)

    @Query("DELETE FROM favorites WHERE contentText = :text")
    suspend fun deleteFavoriteByText(text: String)

    @Query("SELECT * FROM daily_progress ORDER BY date DESC LIMIT 7")
    fun getLastWeekProgress(): Flow<List<DailyProgress>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: DailyProgress)

    @Query("SELECT * FROM cached_images ORDER BY timestamp DESC LIMIT 20")
    suspend fun getCachedImages(): List<CachedImage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedImages(images: List<CachedImage>)

    @Query("DELETE FROM cached_images WHERE url NOT IN (SELECT url FROM cached_images ORDER BY timestamp DESC LIMIT 20)")
    suspend fun trimCachedImages()

    @Query("SELECT * FROM cached_quotes WHERE lang = :lang")
    suspend fun getCachedQuotes(lang: String): List<CachedQuote>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedQuotes(quotes: List<CachedQuote>)
}

@Database(entities = [Habit::class, DailyMission::class, FavoriteItem::class, DailyProgress::class, CachedImage::class, CachedQuote::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun motivatorDao(): MotivatorDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "motivator_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
