package com.ruben.randomquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigation3.AdaptiveNavDisplay
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.NavEntry
import androidx.room.Room
import com.ruben.randomquest.data.AppDatabase
import com.ruben.randomquest.data.QuestRepository
import com.ruben.randomquest.ui.screens.ProfileScreen
import com.ruben.randomquest.ui.screens.QuestGeneratorScreen
import com.ruben.randomquest.ui.screens.QuestNavKey
import com.ruben.randomquest.ui.theme.RandomQuestTheme
import com.ruben.randomquest.ui.viewmodel.QuestViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
        
        val repository = QuestRepository(db.questDao())

        setContent {
            RandomQuestTheme {
                val viewModel: QuestViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return QuestViewModel(repository) as T
                        }
                    }
                )
                
                RandomQuestApp(viewModel)
            }
        }
    }
}

@Composable
fun RandomQuestApp(viewModel: QuestViewModel) {
    val backStack = remember { mutableStateListOf<QuestNavKey>(QuestNavKey.Generator) }
    val currentKey = backStack.last()

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            item(
                selected = currentKey == QuestNavKey.Generator,
                onClick = { 
                    if (currentKey != QuestNavKey.Generator) {
                        backStack.clear()
                        backStack.add(QuestNavKey.Generator)
                    }
                },
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("Home") }
            )
            item(
                selected = currentKey == QuestNavKey.Profile,
                onClick = {
                    if (currentKey != QuestNavKey.Profile) {
                        backStack.clear()
                        backStack.add(QuestNavKey.Profile)
                    }
                },
                icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                label = { Text("Profile") }
            )
        }
    ) {
        AdaptiveNavDisplay(
            backStack = backStack,
            onBack = { if (backStack.size > 1) backStack.removeLast() },
            modifier = Modifier.fillMaxSize()
        ) { key ->
            when (key) {
                is QuestNavKey.Generator -> NavEntry(key) {
                    QuestGeneratorScreen(viewModel = viewModel)
                }
                is QuestNavKey.Profile -> NavEntry(key) {
                    ProfileScreen(viewModel = viewModel)
                }
            }
        }
    }
}
