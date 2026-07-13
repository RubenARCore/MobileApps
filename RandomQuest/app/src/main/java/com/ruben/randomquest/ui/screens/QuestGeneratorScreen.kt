package com.ruben.randomquest.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ruben.randomquest.model.EnergyLevel
import com.ruben.randomquest.model.Quest
import com.ruben.randomquest.model.QuestCategory
import com.ruben.randomquest.ui.viewmodel.QuestViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuestGeneratorScreen(viewModel: QuestViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val completedCount by viewModel.completedCount.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize()
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
                StatCard(label = "Completions", value = completedCount.toString())
                StatCard(label = "Streak", value = "${uiState.streak} 🔥")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Filters
            Text("Filter by Category", style = MaterialTheme.typography.titleMedium)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                QuestCategory.entries.forEach { category ->
                    FilterChip(
                        selected = uiState.selectedCategory == category,
                        onClick = { viewModel.setCategory(if (uiState.selectedCategory == category) null else category) },
                        label = { Text(category.name) },
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Energy Level", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                EnergyLevel.entries.forEach { level ->
                    FilterChip(
                        selected = uiState.selectedEnergyLevel == level,
                        onClick = { viewModel.setEnergyLevel(if (uiState.selectedEnergyLevel == level) null else level) },
                        label = { Text(level.name) },
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Main Action Button
            Button(
                onClick = { viewModel.generateQuest() },
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp),
                shape = MaterialTheme.shapes.extraLarge,
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "GENERATE",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text("QUEST", style = MaterialTheme.typography.bodyLarge)
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
                Text("Recently Completed", style = MaterialTheme.typography.titleMedium)
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
fun StatCard(label: String, value: String) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.labelSmall)
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
                    QuestCategory.FITNESS -> "https://img.icons8.com/color/96/dumbbell.png"
                    QuestCategory.CREATIVE -> "https://img.icons8.com/color/96/paint-palette.png"
                    QuestCategory.MINDFUL -> "https://img.icons8.com/color/96/yoga.png"
                }
                AsyncImage(
                    model = iconUrl,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(quest.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("${quest.category} • ${quest.energyLevel}", style = MaterialTheme.typography.labelMedium)
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "I just got a new quest: ${quest.title}! #RandomQuest")
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
                    Text("Reroll")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onComplete) {
                    Text("Complete")
                }
            }
        }
    }
}
