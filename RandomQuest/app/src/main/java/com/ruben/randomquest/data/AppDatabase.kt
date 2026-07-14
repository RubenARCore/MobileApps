package com.ruben.randomquest.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ruben.randomquest.model.Quest

@Database(entities = [Quest::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questDao(): QuestDao

    companion object {
        const val DATABASE_NAME = "random_quest_db"
    }
}
