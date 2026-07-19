package com.ruben.balkanclickergame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.ruben.balkanclickergame.presentation.components.AchievementPopup
import com.ruben.balkanclickergame.presentation.manager.AchievementNotificationManager
import com.ruben.balkanclickergame.presentation.navigation.Routes
import com.ruben.balkanclickergame.presentation.screens.DashboardScreen
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
    val backStack = rememberNavBackStack(Routes.Dashboard)
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()

    val currentAchievement by achievementNotificationManager.currentAchievement.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Row(modifier = Modifier.padding(innerPadding)) {
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
                            DashboardScreen(homeViewModel = homeViewModel, shopViewModel = shopViewModel)
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




