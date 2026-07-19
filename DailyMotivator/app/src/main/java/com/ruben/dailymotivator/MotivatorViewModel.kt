package com.ruben.dailymotivator

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MotivatorViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).motivatorDao()

    val allHabits: Flow<List<Habit>> = dao.getAllHabits()
    val allMissions: Flow<List<DailyMission>> = dao.getActiveMissions() 
    val activeMissions: Flow<List<DailyMission>> = dao.getActiveMissions()
    val completedMissions: Flow<List<DailyMission>> = dao.getCompletedMissions()
    val allFavorites: Flow<List<FavoriteItem>> = dao.getAllFavorites()
    val weeklyProgress: Flow<List<DailyProgress>> = dao.getLastWeekProgress()

    private val _currentQuote = MutableStateFlow<Quote?>(null)
    val currentQuote: StateFlow<Quote?> = _currentQuote.asStateFlow()

    private val _currentPhrase = MutableStateFlow<Phrase?>(null)
    val currentPhrase: StateFlow<Phrase?> = _currentPhrase.asStateFlow()

    private val _currentFact = MutableStateFlow<Fact?>(null)
    val currentFact: StateFlow<Fact?> = _currentFact.asStateFlow()

    private val _natureImage = MutableStateFlow<String?>(null)
    val natureImage: StateFlow<String?> = _natureImage.asStateFlow()

    private val _natureDescription = MutableStateFlow<String?>(null)
    val natureDescription: StateFlow<String?> = _natureDescription.asStateFlow()

    private val _backgroundImage = MutableStateFlow<String?>(null)
    val backgroundImage: StateFlow<String?> = _backgroundImage.asStateFlow()

    fun updateQuote(quote: Quote) { _currentQuote.value = quote }
    fun updatePhrase(phrase: Phrase) { _currentPhrase.value = phrase }
    fun updateFact(fact: Fact) { _currentFact.value = fact }
    fun updateNatureInfo(url: String?, description: String?) { 
        _natureImage.value = url 
        _natureDescription.value = description
    }
    fun updateBackgroundImage(url: String?) { _backgroundImage.value = url }

    fun addHabit(nameEn: String, nameBg: String) {
        viewModelScope.launch {
            dao.insertHabit(Habit(nameEn = nameEn, nameBg = nameBg))
        }
    }

    fun updateHabit(habit: Habit) {
        viewModelScope.launch {
            dao.updateHabit(habit)
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            dao.deleteHabit(habit)
        }
    }

    fun addMission(text: String, reminderTime: String?) {
        viewModelScope.launch {
            dao.insertMission(DailyMission(textEn = text, textBg = text, reminderTime = reminderTime))
        }
    }

    fun completeMission(mission: DailyMission) {
        viewModelScope.launch {
            dao.updateMission(mission.copy(isCompleted = true))
        }
    }

    fun addFavorite(item: Favoriteable, lang: String) {
        viewModelScope.launch {
            dao.insertFavorite(
                FavoriteItem(
                    contentText = item.contentText,
                    typeKey = item.typeKey,
                    lang = lang
                )
            )
        }
    }

    fun removeFavorite(text: String) {
        viewModelScope.launch {
            dao.deleteFavoriteByText(text)
        }
    }

    fun recordProgress(count: Int) {
        viewModelScope.launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            dao.insertProgress(DailyProgress(today, count))
        }
    }

    suspend fun getOfflineImages(): List<String> = dao.getCachedImages().map { it.url }
    
    suspend fun cacheImages(urls: List<String>) {
        dao.insertCachedImages(urls.map { CachedImage(it) })
        dao.trimCachedImages()
    }

    suspend fun getOfflineQuotes(lang: String): List<Quote> = 
        dao.getCachedQuotes(lang).map { Quote(it.text, it.author, it.lang) }

    suspend fun cacheQuotes(quotes: List<Quote>) {
        dao.insertCachedQuotes(quotes.map { CachedQuote(text = it.text, author = it.author, lang = it.lang) })
    }
}
