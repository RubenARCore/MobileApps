package com.ruben.randomquest.ui.screens

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import com.ruben.randomquest.R
import com.ruben.randomquest.model.QuestCategory
import com.ruben.randomquest.ui.components.AppBackground
import com.ruben.randomquest.ui.components.GlassCard
import com.ruben.randomquest.ui.components.StatCard
import com.ruben.randomquest.ui.utils.getLabel
import com.ruben.randomquest.ui.viewmodel.QuestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: QuestViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val completedCount by viewModel.completedCount.collectAsState()
    
    val currentLocale = AppCompatDelegate.getApplicationLocales().toLanguageTags()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.title_progress).uppercase(),
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { innerPadding ->
        AppBackground {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Bento Stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        label = stringResource(R.string.stat_total_quests),
                        value = completedCount.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = stringResource(R.string.stat_current_streak),
                        value = uiState.streak.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Language Glass Box
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Text(
                            stringResource(R.string.settings_language),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = currentLocale.startsWith("en"),
                                onClick = { 
                                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("en"))
                                },
                                label = { Text(stringResource(R.string.lang_en)) }
                            )
                            FilterChip(
                                selected = currentLocale.startsWith("bg") || currentLocale.isEmpty(),
                                onClick = { 
                                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("bg"))
                                },
                                label = { Text(stringResource(R.string.lang_bg)) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    stringResource(R.string.label_history),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start)
                )
                
                Spacer(modifier = Modifier.height(12.dp))

                if (uiState.recentlyCompleted.isEmpty()) {
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(stringResource(R.string.empty_history), style = MaterialTheme.typography.bodyLarge)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.recentlyCompleted) { quest ->
                            GlassCard(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(quest.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                                        Text(quest.category.getLabel(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary) 
                                    }
                                    Text("✅", modifier = Modifier.padding(start = 8.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

