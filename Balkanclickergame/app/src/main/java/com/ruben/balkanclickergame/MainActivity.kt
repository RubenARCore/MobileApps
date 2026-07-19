package com.ruben.balkanclickergame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.window.core.layout.WindowWidthSizeClass
import com.ruben.balkanclickergame.presentation.components.AchievementPopup
import com.ruben.balkanclickergame.presentation.manager.AchievementNotificationManager
import com.ruben.balkanclickergame.presentation.navigation.Routes
import com.ruben.balkanclickergame.presentation.screens.DashboardScreen
import com.ruben.balkanclickergame.presentation.screens.ShopScreen
import com.ruben.balkanclickergame.presentation.viewmodels.HomeViewModel
import com.ruben.balkanclickergame.presentation.viewmodels.ShopViewModel
import com.ruben.balkanclickergame.ui.theme.BalkanClickerGameTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var achievementNotificationManager: AchievementNotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BalkanClickerGameTheme {
                MainScreen(achievementNotificationManager)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainScreen(achievementNotificationManager: AchievementNotificationManager) {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val isExpanded = adaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED
    
    val backStack = rememberNavBackStack(Routes.Dashboard)
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()

    val currentAchievement by achievementNotificationManager.currentAchievement.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                if (!isExpanded) {
                    NavigationBar {
                        NavigationBarItem(
                            selected = backStack.contains(Routes.Dashboard) && backStack.last() == Routes.Dashboard,
                            onClick = {
                                if (backStack.last() != Routes.Dashboard) {
                                    backStack.add(Routes.Dashboard)
                                }
                            },
                            icon = { Icon(Icons.Rounded.Dashboard, contentDescription = null) },
                            label = { Text(stringResource(R.string.dashboard_title)) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Row(modifier = Modifier.padding(innerPadding)) {
                if (isExpanded) {
                    NavigationRail {
                        NavigationRailItem(
                            selected = backStack.contains(Routes.Dashboard) && backStack.last() == Routes.Dashboard,
                            onClick = {
                                if (backStack.last() != Routes.Dashboard) {
                                    backStack.add(Routes.Dashboard)
                                }
                            },
                            icon = { Icon(Icons.Rounded.Dashboard, contentDescription = null) },
                            label = { Text(stringResource(R.string.dashboard_title)) }
                        )
                        NavigationRailItem(
                            selected = backStack.contains(Routes.Shop),
                            onClick = {
                                if (!backStack.contains(Routes.Shop)) {
                                    backStack.add(Routes.Shop)
                                } else if (backStack.last() != Routes.Shop) {
                                    backStack.remove(Routes.Shop)
                                    backStack.add(Routes.Shop)
                                }
                            },
                            icon = { Icon(Icons.Rounded.ShoppingCart, contentDescription = null) },
                            label = { Text(stringResource(R.string.shop_title)) }
                        )
                    }
                }

                NavDisplay(
                    backStack = backStack,
                    onBack = { backStack.removeLastOrNull() },
                    sceneStrategy = listDetailStrategy,
                    entryDecorators = listOf(
                        rememberViewModelStoreNavEntryDecorator(),
                        androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator()
                    ),
                    entryProvider = entryProvider {
                        entry<Routes.Dashboard>(
                            metadata = ListDetailSceneStrategy.listPane()
                        ) {
                            val homeViewModel: HomeViewModel = viewModel()
                            val shopViewModel: ShopViewModel = viewModel()
                            DashboardScreen(
                                homeViewModel = homeViewModel,
                                shopViewModel = if (!isExpanded) shopViewModel else null
                            )
                        }
                        entry<Routes.Shop>(
                            metadata = ListDetailSceneStrategy.detailPane()
                        ) {
                            val shopViewModel: ShopViewModel = viewModel()
                            ShopScreen(viewModel = shopViewModel)
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        AchievementPopup(
            achievement = currentAchievement,
            onDismiss = { achievementNotificationManager.dismiss() },
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}
