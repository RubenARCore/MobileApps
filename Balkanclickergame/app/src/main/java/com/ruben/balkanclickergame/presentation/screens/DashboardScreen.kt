package com.ruben.balkanclickergame.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ruben.balkanclickergame.R
import com.ruben.balkanclickergame.domain.model.RegionalThemes
import com.ruben.balkanclickergame.presentation.components.UpgradeCard
import com.ruben.balkanclickergame.presentation.manager.GameEvent
import com.ruben.balkanclickergame.presentation.viewmodels.HomeViewModel
import com.ruben.balkanclickergame.presentation.viewmodels.ShopViewModel
import com.ruben.balkanclickergame.presentation.viewmodels.UpgradeUiModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun DashboardScreen(
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    shopViewModel: ShopViewModel? = null
) {
    val gameState by homeViewModel.gameState.collectAsState()
    val currentEvent by homeViewModel.currentEvent
    val shopItems by shopViewModel?.shopItems?.collectAsState() ?: remember { mutableStateOf(emptyList()) }

    val regionalTheme = RegionalThemes.getThemeForLifetimeCoins(gameState.lifetimeCoins)

    DashboardContent(
        score = gameState.score,
        clickPower = gameState.clickPower,
        passiveIncome = gameState.totalPassiveIncome,
        lifetimeCoins = gameState.lifetimeCoins,
        lifetimeClicks = gameState.lifetimeClicks,
        prestigePoints = gameState.prestigePoints,
        prestigeMultiplier = gameState.prestigeMultiplier,
        regionalTheme = regionalTheme,
        currentEvent = currentEvent,
        modifier = modifier.fillMaxSize(),
        shopItems = shopItems,
        onCoinClick = homeViewModel::onCoinClicked,
        onEmigrateClick = homeViewModel::onEmigrateClicked,
        onBuyUpgrade = { shopViewModel?.purchaseUpgrade(it) }
    )
}

@Composable
fun DashboardContent(
    score: Long,
    clickPower: Long,
    passiveIncome: Long,
    lifetimeCoins: Long,
    lifetimeClicks: Long,
    prestigePoints: Long,
    prestigeMultiplier: Double,
    regionalTheme: com.ruben.balkanclickergame.domain.model.RegionalTheme,
    currentEvent: GameEvent,
    modifier: Modifier = Modifier,
    shopItems: List<UpgradeUiModel> = emptyList(),
    onCoinClick: () -> Unit,
    onEmigrateClick: () -> Unit,
    onBuyUpgrade: (String) -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var scale by remember { mutableFloatStateOf(1f) }
    val animatedScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "CoinScale"
    )

    val floatingTexts = remember { mutableStateListOf<FloatingTextItem>() }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.dashboard_title),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (prestigePoints > 0) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = CircleShape
                    ) {
                        Text(
                            text = stringResource(R.string.prestige_multiplier_format, prestigeMultiplier),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    shape = MaterialTheme.shapes.large
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp)
                    ) {
                        Text(
                            text = "$score",
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = regionalTheme.currencySymbol,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Surface(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = CircleShape
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.TouchApp,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stringResource(R.string.dashboard_click_power_format, clickPower),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = regionalTheme.currencyIcon,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stringResource(R.string.dashboard_passive_income_format, passiveIncome),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Emigration Section
        if (lifetimeCoins >= 100000 || prestigePoints > 0) {
            item {
                Text(
                    text = stringResource(R.string.section_prestige),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.prestige_emigrate_button),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.prestige_emigrate_desc),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = onEmigrateClick,
                            enabled = lifetimeCoins >= 100000
                        ) {
                            Text(stringResource(R.string.prestige_emigrate_button))
                        }
                    }
                }
            }
        }

        // Event Banner
        if (currentEvent !is GameEvent.None) {
            item {
                Text(
                    text = stringResource(R.string.section_events),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                AnimatedVisibility(
                    visible = true,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    val containerColor = if (currentEvent is GameEvent.Wedding) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.errorContainer
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = containerColor),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = stringResource(
                                    if (currentEvent is GameEvent.Wedding) R.string.event_wedding_title else R.string.event_inflation_title
                                ),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(
                                    if (currentEvent is GameEvent.Wedding) R.string.event_wedding_desc else R.string.event_inflation_desc
                                ),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }

        // The Clickable Coin
        item {
            Box(
                modifier = Modifier
                    .padding(vertical = 32.dp)
                    .size(240.dp)
                    .scale(animatedScale)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onCoinClick()
                        coroutineScope.launch {
                            scale = 0.9f
                            delay(50)
                            scale = 1.1f
                            delay(50)
                            scale = 1f
                        }

                        val id = System.currentTimeMillis()
                        val xOffset = Random.nextInt(-100, 100).dp
                        val yOffset = Random.nextInt(-100, 100).dp
                        val floatingText =
                            context.getString(R.string.dashboard_floating_text_format, clickPower)
                        floatingTexts.add(FloatingTextItem(id, floatingText, xOffset, yOffset))
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = regionalTheme.currencyIcon,
                    contentDescription = stringResource(R.string.dashboard_coin_description),
                    modifier = Modifier.fillMaxSize(),
                    tint = Color(0xFFFFD700)
                )

                floatingTexts.forEach { item ->
                    key(item.id) {
                        FloatingText(
                            text = item.text,
                            xOffset = item.xOffset,
                            yOffset = item.yOffset,
                            onAnimationEnd = { floatingTexts.remove(item) }
                        )
                    }
                }
            }
        }

        // Shop Section
        if (shopItems.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.section_upgrades),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            items(shopItems, key = { it.upgrade.id }) { uiModel ->
                UpgradeCard(
                    uiModel = uiModel,
                    onBuyClick = { onBuyUpgrade(uiModel.upgrade.id) }
                )
            }
        }

        // Lifetime Stats
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
            ) {
                Text(
                    text = stringResource(R.string.dashboard_lifetime_stats),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline
                )
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.dashboard_total_earned, lifetimeCoins),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = stringResource(R.string.dashboard_total_clicks, lifetimeClicks),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}


data class FloatingTextItem(
    val id: Long,
    val text: String,
    val xOffset: androidx.compose.ui.unit.Dp,
    val yOffset: androidx.compose.ui.unit.Dp
)

@Composable
fun FloatingText(
    text: String,
    xOffset: androidx.compose.ui.unit.Dp,
    yOffset: androidx.compose.ui.unit.Dp,
    onAnimationEnd: () -> Unit
) {
    val animatableY = remember { Animatable(0f) }
    val animatableAlpha = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        launch {
            animatableY.animateTo(
                targetValue = -200f,
                animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
            )
            onAnimationEnd()
        }
        launch {
            animatableAlpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
            )
        }
    }

    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        ),
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .offset(x = xOffset, y = yOffset + animatableY.value.dp)
            .scale(1.2f)
            .alpha(animatableAlpha.value)
    )
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun DashboardScreenPreview() {
    MaterialTheme {
        DashboardContent(
            score = 1250L,
            clickPower = 10L,
            passiveIncome = 5L,
            lifetimeCoins = 5000L,
            lifetimeClicks = 120L,
            prestigePoints = 0,
            prestigeMultiplier = 1.0,
            regionalTheme = com.ruben.balkanclickergame.domain.model.RegionalThemes.BGN,
            currentEvent = GameEvent.None,
            onCoinClick = {},
            onEmigrateClick = {}
        )
    }
}
