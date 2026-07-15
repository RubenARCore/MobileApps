package com.ruben.randomquest.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruben.randomquest.data.QuestRepository
import com.ruben.randomquest.model.Quest
import com.ruben.randomquest.model.QuestCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class QuestUiState(
    val activeQuest: Quest? = null,
    val selectedCategory: QuestCategory? = null,
    val totalCompleted: Int = 0,
    val streak: Int = 0,
    val isLoading: Boolean = false,
    val recentlyCompleted: List<Quest> = emptyList()
)

class QuestViewModel(private val repository: QuestRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(QuestUiState())
    val uiState: StateFlow<QuestUiState> = _uiState.asStateFlow()

    val completedCount: StateFlow<Int> = repository.getCompletedQuestsCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    init {
        viewModelScope.launch {
            repository.prepopulateIfNeeded()
            updateStats()
            repository.getAllQuests().collect { quests ->
                _uiState.value = _uiState.value.copy(
                    recentlyCompleted = quests.filter { it.isCompleted }.sortedByDescending { it.completedAt }.take(5)
                )
            }
        }
    }

    fun setCategory(category: QuestCategory?) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun generateQuest() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val currentLanguage = androidx.appcompat.app.AppCompatDelegate.getApplicationLocales().toLanguageTags()
                .ifEmpty { "bg" }.take(2)
            val quest = repository.getRandomQuest(
                currentLanguage,
                _uiState.value.selectedCategory
            )
            _uiState.value = _uiState.value.copy(activeQuest = quest, isLoading = false)
        }
    }

    fun completeQuest() {
        val currentQuest = _uiState.value.activeQuest ?: return
        viewModelScope.launch {
            repository.completeQuest(currentQuest)
            _uiState.value = _uiState.value.copy(activeQuest = null)
            updateStats()
        }
    }

    fun rerollQuest() {
        generateQuest()
    }

    private fun updateStats() {
        viewModelScope.launch {
            val streak = repository.getStreak()
            _uiState.value = _uiState.value.copy(streak = streak)
        }
    }
}
