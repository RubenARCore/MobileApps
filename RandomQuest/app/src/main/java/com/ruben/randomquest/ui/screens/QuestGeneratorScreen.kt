package com.ruben.randomquest.ui.screens

import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Statistics Tiles (Bento Style)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        label = stringResource(R.string.stat_completions),
                        value = completedCount.toString(),
                        icon = "✅",
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = stringResource(R.string.stat_streak),
                        value = uiState.streak.toString(),
                        icon = "🔥",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Active Quest Section (Takes up available space and stays centered)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = uiState.activeQuest != null,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
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

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom Fixed Section: Categories and Generate Button
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        stringResource(R.string.filter_category),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // Category Chips in Grid (Fixed height to prevent shifting)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.height(180.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        userScrollEnabled = false // Keep it static
                    ) {
                        items(QuestCategory.entries) { category ->
                            val isSelected = uiState.selectedCategory == category
                            val label = category.getLabel()

                            CategoryTile(
                                label = label,
                                isSelected = isSelected,
                                onClick = { viewModel.setCategory(if (isSelected) null else category) }
                            )
                        }
                    }

                    Button(
                        onClick = { viewModel.generateQuest() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
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

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight() // Allow it to wrap but stay within parent Box bounds
    ) {
        val isWelcome = quest.id == -1L
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp) // Slight internal padding
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (!isWelcome) {
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
                }
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        quest.title,
                        style = if (isWelcome) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = if (isWelcome) TextAlign.Center else TextAlign.Start,
                        modifier = if (isWelcome) Modifier.fillMaxWidth() else Modifier
                    )
                    if (!isWelcome) {
                        Text(
                            quest.category.getLabel(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
                
                if (!isWelcome) {
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
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Scrollable description area to keep card height manageable
            Box(
                modifier = Modifier
                    .weight(1f, fill = false) // Take space if needed, but don't force expansion
                    .heightIn(max = 200.dp) // Safety cap for very long descriptions
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                contentAlignment = if (isWelcome) Alignment.Center else Alignment.TopStart
            ) {
                Text(
                    quest.description,
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = 24.sp,
                    textAlign = if (isWelcome) TextAlign.Center else TextAlign.Start
                )
            }

            if (!isWelcome) {
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
}

