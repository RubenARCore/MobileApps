package com.ruben.randomquest.ui.screens

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.ruben.randomquest.R
import com.ruben.randomquest.model.EnergyLevel
import com.ruben.randomquest.model.QuestCategory
import com.ruben.randomquest.ui.components.StatCard
import com.ruben.randomquest.ui.viewmodel.QuestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: QuestViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val completedCount by viewModel.completedCount.collectAsState()
    
    val currentLocale = AppCompatDelegate.getApplicationLocales().toLanguageTags()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(title = { Text(stringResource(R.string.title_progress)) })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatCard(label = stringResource(R.string.stat_total_quests), value = completedCount.toString())
                StatCard(label = stringResource(R.string.stat_current_streak), value = stringResource(R.string.streak_days, uiState.streak))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Language Selector
            Text(
                stringResource(R.string.settings_language),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                FilterChip(
                    selected = currentLocale.startsWith("en"),
                    onClick = { 
                        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("en"))
                    },
                    label = { Text(stringResource(R.string.lang_en)) },
                    modifier = Modifier.padding(4.dp)
                )
                FilterChip(
                    selected = currentLocale.startsWith("bg") || currentLocale.isEmpty(),
                    onClick = { 
                        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("bg"))
                    },
                    label = { Text(stringResource(R.string.lang_bg)) },
                    modifier = Modifier.padding(4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                stringResource(R.string.label_history),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.Start)
            )
            
            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.recentlyCompleted.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.empty_history), style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(uiState.recentlyCompleted) { quest ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            ListItem(
                                headlineContent = { Text(quest.title, fontWeight = FontWeight.Bold) },
                                supportingContent = { 
                                    val categoryLabel = when (quest.category) {
                                        QuestCategory.SOCIAL -> stringResource(R.string.cat_social)
                                        QuestCategory.FITNESS -> stringResource(R.string.cat_fitness)
                                        QuestCategory.CREATIVE -> stringResource(R.string.cat_creative)
                                        QuestCategory.MINDFUL -> stringResource(R.string.cat_mindful)
                                    }
                                    val energyLabel = when (quest.energyLevel) {
                                        EnergyLevel.LOW -> stringResource(R.string.energy_low)
                                        EnergyLevel.MEDIUM -> stringResource(R.string.energy_medium)
                                        EnergyLevel.HIGH -> stringResource(R.string.energy_high)
                                    }
                                    Text("$categoryLabel • $energyLabel") 
                                },
                                trailingContent = { Text("✅") },
                                colors = ListItemDefaults.colors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
                            )
                        }
                    }
                }
            }
        }
    }
}
