package com.ruben.randomquest.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ruben.randomquest.R
import com.ruben.randomquest.model.EnergyLevel
import com.ruben.randomquest.model.Quest
import com.ruben.randomquest.model.QuestCategory
import com.ruben.randomquest.ui.components.StatCard
import com.ruben.randomquest.ui.viewmodel.QuestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestGeneratorScreen(viewModel: QuestViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val completedCount by viewModel.completedCount.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "🎲 Случайни",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Предизвикателства".uppercase(),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatCard(label = stringResource(R.string.stat_completions), value = completedCount.toString())
                StatCard(label = stringResource(R.string.stat_streak), value = "${uiState.streak} 🔥")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Filters
            Text(stringResource(R.string.filter_category), style = MaterialTheme.typography.titleMedium)
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val chunks = QuestCategory.entries.chunked(3)
                chunks.forEach { rowCategories ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowCategories.forEach { category ->
                            val categoryLabel = when (category) {
                                QuestCategory.SOCIAL -> stringResource(R.string.cat_social)
                                QuestCategory.FUN -> stringResource(R.string.cat_fun)
                                QuestCategory.FITNESS -> stringResource(R.string.cat_fitness)
                                QuestCategory.LOVE -> stringResource(R.string.cat_love)
                                QuestCategory.EXTREME -> stringResource(R.string.cat_extreme)
                                QuestCategory.KNOWLEDGE -> stringResource(R.string.cat_knowledge)
                            }
                            FilterChip(
                                selected = uiState.selectedCategory == category,
                                onClick = { viewModel.setCategory(if (uiState.selectedCategory == category) null else category) },
                                label = {
                                    Text(
                                        categoryLabel,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(stringResource(R.string.filter_energy), style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EnergyLevel.entries.forEach { level ->
                    val levelLabel = when (level) {
                        EnergyLevel.LOW -> stringResource(R.string.energy_low)
                        EnergyLevel.MEDIUM -> stringResource(R.string.energy_medium)
                        EnergyLevel.HIGH -> stringResource(R.string.energy_high)
                    }
                    FilterChip(
                        selected = uiState.selectedEnergyLevel == level,
                        onClick = { viewModel.setEnergyLevel(if (uiState.selectedEnergyLevel == level) null else level) },
                        label = {
                            Text(
                                levelLabel,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Main Action Button
            Button(
                onClick = { viewModel.generateQuest() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(horizontal = 16.dp),
                shape = MaterialTheme.shapes.medium,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 2.dp
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        stringResource(R.string.btn_generate),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        stringResource(R.string.btn_quest),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Active Quest Dashboard
            AnimatedVisibility(visible = uiState.activeQuest != null) {
                uiState.activeQuest?.let { quest ->
                    ActiveQuestCard(
                        quest = quest,
                        onComplete = { viewModel.completeQuest() },
                        onReroll = { viewModel.rerollQuest() }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Recently Completed
            if (uiState.recentlyCompleted.isNotEmpty()) {
                Text(stringResource(R.string.label_recently_completed), style = MaterialTheme.typography.titleMedium)
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(uiState.recentlyCompleted) { quest ->
                        ListItem(
                            headlineContent = { Text(quest.title) },
                            supportingContent = { Text(quest.category.name) },
                            trailingContent = { Text("✅") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActiveQuestCard(quest: Quest, onComplete: () -> Unit, onReroll: () -> Unit) {
    val context = LocalContext.current
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Placeholder for category icon
                val iconUrl = when (quest.category) {
                    QuestCategory.SOCIAL -> "https://img.icons8.com/color/96/conference-call.png"
                    QuestCategory.FUN -> "https://img.icons8.com/color/96/clown-fish.png"
                    QuestCategory.FITNESS -> "https://img.icons8.com/color/96/dumbbell.png"
                    QuestCategory.LOVE -> "https://img.icons8.com/color/96/filled-heart.png"
                    QuestCategory.EXTREME -> "https://img.icons8.com/color/96/mountain.png"
                    QuestCategory.KNOWLEDGE -> "https://img.icons8.com/color/96/book.png"
                }
                AsyncImage(
                    model = iconUrl,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(quest.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    val categoryLabel = when (quest.category) {
                        QuestCategory.SOCIAL -> stringResource(R.string.cat_social)
                        QuestCategory.FUN -> stringResource(R.string.cat_fun)
                        QuestCategory.FITNESS -> stringResource(R.string.cat_fitness)
                        QuestCategory.LOVE -> stringResource(R.string.cat_love)
                        QuestCategory.EXTREME -> stringResource(R.string.cat_extreme)
                        QuestCategory.KNOWLEDGE -> stringResource(R.string.cat_knowledge)
                    }
                    val energyLabel = when (quest.energyLevel) {
                        EnergyLevel.LOW -> stringResource(R.string.energy_low)
                        EnergyLevel.MEDIUM -> stringResource(R.string.energy_medium)
                        EnergyLevel.HIGH -> stringResource(R.string.energy_high)
                    }
                    Text("$categoryLabel • $energyLabel", style = MaterialTheme.typography.labelMedium)
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_message, quest.title))
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                }) {
                    Icon(Icons.Default.Share, contentDescription = "Share")
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Text(quest.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onReroll) {
                    Text(stringResource(R.string.btn_reroll))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onComplete) {
                    Text(stringResource(R.string.btn_complete))
                }
            }
        }
    }
}
