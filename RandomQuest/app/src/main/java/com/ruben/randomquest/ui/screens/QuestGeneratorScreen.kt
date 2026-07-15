package com.ruben.randomquest.ui.screens

import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ruben.randomquest.R
import com.ruben.randomquest.model.Quest
import com.ruben.randomquest.model.QuestCategory
import com.ruben.randomquest.ui.components.AppBackground
import com.ruben.randomquest.ui.components.ConfettiEffect
import com.ruben.randomquest.ui.components.GlassCard
import com.ruben.randomquest.ui.components.StatCard
import com.ruben.randomquest.ui.utils.getLabel
import com.ruben.randomquest.ui.viewmodel.QuestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestGeneratorScreen(viewModel: QuestViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val completedCount by viewModel.completedCount.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.app_name).uppercase(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { innerPadding ->
        AppBackground {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Statistics Tiles (Bento Style)
                item {
                    StatCard(
                        label = stringResource(R.string.stat_completions),
                        value = completedCount.toString(),
                        icon = "✅"
                    )
                }
                item {
                    StatCard(
                        label = stringResource(R.string.stat_streak),
                        value = uiState.streak.toString(),
                        icon = "🔥"
                    )
                }

                // Active Quest (Spans 2 columns)
                item(span = { GridItemSpan(2) }) {
                    AnimatedVisibility(
                        visible = uiState.activeQuest != null,
                        enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                    ) {
                        uiState.activeQuest?.let { quest ->
                            ActiveQuestBentoCard(
                                quest = quest,
                                onComplete = { viewModel.completeQuest() },
                                onReroll = { viewModel.rerollQuest() }
                            )
                        }
                    }
                }

                // Categories Header
                item(span = { GridItemSpan(2) }) {
                    Text(
                        stringResource(R.string.filter_category),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // Category Chips in Grid
                items(QuestCategory.entries) { category ->
                    val isSelected = uiState.selectedCategory == category
                    val label = category.getLabel()
                    
                    CategoryTile(
                        label = label,
                        isSelected = isSelected,
                        onClick = { viewModel.setCategory(if (isSelected) null else category) }
                    )
                }

                // Generate Button (Spans 2 columns)
                item(span = { GridItemSpan(2) }) {
                    Button(
                        onClick = { viewModel.generateQuest() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                    ) {
                        Text(
                            stringResource(R.string.btn_generate).uppercase(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                // Bottom Spacer
                item(span = { GridItemSpan(2) }) {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }

            if (uiState.showCelebration) {
                ConfettiEffect()
            }
        }
    }
}


@Composable
fun CategoryTile(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.height(52.dp),
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.1f),
        border = if (isSelected) null else BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ActiveQuestBentoCard(quest: Quest, onComplete: () -> Unit, onReroll: () -> Unit) {
    val context = LocalContext.current
    
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                    modifier = Modifier.size(56.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        quest.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        quest.category.getLabel(),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                IconButton(onClick = {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_message, quest.title))
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                }) {
                    Icon(Icons.Default.Share, contentDescription = "Share", tint = MaterialTheme.colorScheme.primary)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                quest.description,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onReroll,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.btn_reroll))
                }
                Button(
                    onClick = onComplete,
                    modifier = Modifier.weight(1.5f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(stringResource(R.string.btn_complete))
                }
            }
        }
    }
}
