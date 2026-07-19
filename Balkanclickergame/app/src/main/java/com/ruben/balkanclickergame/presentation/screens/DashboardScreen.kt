package com.ruben.balkanclickergame.presentation.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.material3.*
import com.ruben.balkanclickergame.presentation.viewmodels.UpgradeUiModel
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.res.stringResource
import com.ruben.balkanclickergame.R
import com.ruben.balkanclickergame.domain.model.GameState
import com.ruben.balkanclickergame.presentation.viewmodels.HomeViewModel
import com.ruben.balkanclickergame.presentation.viewmodels.ShopViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun DashboardScreen(
    homeViewModel: HomeViewModel,
    shopViewModel: ShopViewModel,
    modifier: Modifier = Modifier
) {
    val gameState by homeViewModel.gameState.collectAsState()
    val shopItems by shopViewModel.shopItems.collectAsState()

    DashboardContent(
        score = gameState.score,
        clickPower = gameState.clickPower,
        passiveIncome = gameState.totalPassiveIncome,
        lifetimeCoins = gameState.lifetimeCoins,
        lifetimeClicks = gameState.lifetimeClicks,
        prestigePoints = gameState.prestigePoints,
        prestigeMultiplier = gameState.prestigeMultiplier,
        shopItems = shopItems,
        onCoinClick = homeViewModel::onCoinClicked,
        onEmigrateClick = homeViewModel::onEmigrateClicked,
        onBuyUpgrade = { upgradeId -> shopViewModel.purchaseUpgrade(upgradeId) },
        modifier = modifier.fillMaxSize()
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
    shopItems: List<UpgradeUiModel>,
    onCoinClick: () -> Unit,
    onEmigrateClick: () -> Unit,
    onBuyUpgrade: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var scale by remember { mutableFloatStateOf(1f) }
    val animatedScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "CoinScale"
    )

    // Manage floating text items
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
                    Text(
                        text = "$score",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp)
                    )
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
                                imageVector = Icons.Rounded.MonetizationOn,
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

                        // Add floating text
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
                    imageVector = Icons.Rounded.MonetizationOn,
                    contentDescription = stringResource(R.string.dashboard_coin_description),
                    modifier = Modifier.fillMaxSize(),
                    tint = Color(0xFFFFD700) // Golden color
                )

                // Render floating texts inside the coin's box so they are anchored to it
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

        // Bazaar Header
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.ShoppingCart,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.shop_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))
        }

        // Shop items
        items(shopItems) { uiModel ->
            UpgradeCard(
                uiModel = uiModel,
                onBuyClick = { onBuyUpgrade(uiModel.upgrade.id) }
            )
        }

        // Lifetime Stats at the bottom
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

@Composable
fun UpgradeCard(
    uiModel: UpgradeUiModel,
    onBuyClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(uiModel.upgrade.nameResId),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.shop_item_level, uiModel.currentLevel),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                Button(
                    onClick = onBuyClick,
                    enabled = uiModel.canAfford
                ) {
                    Text(stringResource(R.string.shop_item_buy, uiModel.currentCost))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(uiModel.upgrade.descriptionResId),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                if (uiModel.upgrade.passiveIncomePerLevel > 0) {
                    Text(
                        text = stringResource(R.string.shop_item_passive_benefit, uiModel.upgrade.passiveIncomePerLevel),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
                if (uiModel.upgrade.clickPowerPerLevel > 0) {
                    Text(
                        text = stringResource(R.string.shop_item_click_benefit, uiModel.upgrade.clickPowerPerLevel),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
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
            shopItems = emptyList(),
            onCoinClick = {},
            onEmigrateClick = {},
            onBuyUpgrade = {}
        )
    }
}

@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp,orientation=landscape")
@Composable
fun DashboardScreenTabletPreview() {
    MaterialTheme {
        DashboardContent(
            score = 1250L,
            clickPower = 10L,
            passiveIncome = 5L,
            lifetimeCoins = 5000L,
            lifetimeClicks = 120L,
            prestigePoints = 2,
            prestigeMultiplier = 1.2,
            shopItems = emptyList(),
            onCoinClick = {},
            onEmigrateClick = {},
            onBuyUpgrade = {}
        )
    }
}
