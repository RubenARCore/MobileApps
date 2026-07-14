package com.ruben.randomquest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
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
                icon = { Icon(Icons.Default.Home, contentDescription = stringResource(R.string.nav_home)) },
                label = { Text(stringResource(R.string.nav_home)) }
            )
            item(
                selected = currentKey == QuestNavKey.Profile,
                onClick = {
                    if (currentKey != QuestNavKey.Profile) {
                        backStack.clear()
                        backStack.add(QuestNavKey.Profile)
                    }
                },
                icon = { Icon(Icons.Default.Person, contentDescription = stringResource(R.string.nav_profile)) },
                label = { Text(stringResource(R.string.nav_profile)) }
            )
        }
    ) {
        NavDisplay(
            backStack = backStack,
            onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) },
            modifier = Modifier.fillMaxSize(),
            entryProvider = entryProvider {
                entry<QuestNavKey.Generator> {
                    QuestGeneratorScreen(viewModel = viewModel)
                }
                entry<QuestNavKey.Profile> {
                    ProfileScreen(viewModel = viewModel)
                }
            }
        )
    }
}
