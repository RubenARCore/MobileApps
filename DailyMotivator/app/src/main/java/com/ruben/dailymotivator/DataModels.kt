package com.ruben.dailymotivator

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Entity
import androidx.room.PrimaryKey
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


data class PexelsResponse(val photos: List<PexelsPhoto>)
data class PexelsPhoto(val src: PexelsSource)
data class PexelsSource(val large2x: String)

interface PexelsService {
    @GET("v1/search")
    suspend fun searchPhotos(
        @Header("Authorization") auth: String,
        @Query("query") query: String,
        @Query("per_page") perPage: Int = 15
    ): PexelsResponse
}


data class ApiQuote(val q: String, val a: String)

interface QuoteService {
    @GET("api/random")
    suspend fun getRandomQuote(): List<ApiQuote>
}


interface Favoriteable {
    val contentText: String
    val typeKey: String
}

@Entity(tableName = "favorites")
data class FavoriteItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    override val contentText: String,
    override val typeKey: String,
    val author: String? = null,
    val lang: String
) : Favoriteable

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nameEn: String,
    val nameBg: String,
    var isChecked: Boolean = false
)

@Entity(tableName = "missions")
data class DailyMission(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var textEn: String,
    var textBg: String,
    var isCompleted: Boolean = false,
    var reminderTime: String? = null
)

@Entity(tableName = "daily_progress")
data class DailyProgress(
    @PrimaryKey val date: String,
    val completedCount: Int
)

@Entity(tableName = "cached_images")
data class CachedImage(
    @PrimaryKey val url: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "cached_quotes")
data class CachedQuote(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String,
    val author: String,
    val lang: String
)


data class Quote(val text: String, val author: String, val lang: String) : Favoriteable {
    override val contentText: String get() = "$text - $author"
    override val typeKey: String get() = "quote_title"
}

data class Phrase(val text: String, val lang: String) : Favoriteable {
    override val contentText: String get() = text
    override val typeKey: String get() = "phrase_title"
}

data class Fact(val text: String, val lang: String) : Favoriteable {
    override val contentText: String get() = text
    override val typeKey: String get() = "fact_title"
}


enum class Language { BG, EN }

object AppStrings {
    fun get(lang: Language, key: String): String = when (lang) {
        Language.BG -> when (key) {
            "app_title" -> "ДНЕВНА ДОЗА ВДЪХНОВЕНИЕ"
            "home" -> "Начало"
            "fav" -> "Любими"
            "settings" -> "Настройки"
            "refresh" -> "Мотивирай"
            "phrase_title" -> "Мотивираща фраза"
            "quote_title" -> "Цитат на деня"
            "fact_title" -> "Знаете ли, че...?"
            "habits_title" -> "Моите цели"
            "streak" -> "Серия"
            "days" -> "дни поред"
            "mission_title" -> "Дневни задачи"
            "completed" -> "Завършени"
            "not_completed" -> "Активни:"
            "copy_toast" -> "Копирано в клипборда!"
            "no_favs" -> "Все още нямате любими елементи."
            "lang_label" -> "Език"
            "theme_label" -> "Тъмна тема"
            "notif_label" -> "Известия"
            "edit_mission" -> "Добави задача"
            "save" -> "Запази"
            "cancel" -> "Отказ"
            "set_reminder" -> "Подсещане в:"
            "no_completed" -> "Няма завършени задачи още."
            "no_active" -> "Няма незавършени задачи още."
            "achievements" -> "Постижения днес"
            "add_goal" -> "Добави цел"
            "goal_name" -> "Име на целта"
            "day_complete" -> "Денят е завършен!"
            else -> key
        }
        Language.EN -> when (key) {
            "app_title" -> "DAILY DOSE OF INSPIRATION"
            "home" -> "Home"
            "fav" -> "Favorites"
            "settings" -> "Settings"
            "refresh" -> "Refresh All"
            "phrase_title" -> "Motivational Phrase"
            "quote_title" -> "Quote of the Day"
            "fact_title" -> "Did you know...?"
            "habits_title" -> "My Goals"
            "streak" -> "Streak"
            "days" -> "days in a row"
            "mission_title" -> "My Missions"
            "completed" -> "Completed"
            "not_completed" -> "Active:"
            "copy_toast" -> "Copied to clipboard!"
            "no_favs" -> "No favorite items yet."
            "lang_label" -> "Language"
            "theme_label" -> "Dark Mode"
            "notif_label" -> "Notifications"
            "edit_mission" -> "Add Mission"
            "save" -> "Save"
            "cancel" -> "Cancel"
            "set_reminder" -> "Reminder at:"
            "no_completed" -> "No completed missions yet."
            "no_active" -> "No active missions yet."
            "achievements" -> "Today's Achievements"
            "add_goal" -> "Add Goal"
            "goal_name" -> "Goal Name"
            "day_complete" -> "Day Completed!"
            else -> key
        }
    }
}

sealed class Screen(val getTitle: (Language) -> String, val icon: ImageVector) {
    object Home : Screen({ AppStrings.get(it, "home") }, Icons.Default.Home)
    object Favorites : Screen({ AppStrings.get(it, "fav") }, Icons.Default.Favorite)
    object Settings : Screen({ AppStrings.get(it, "settings") }, Icons.Default.Settings)
}
