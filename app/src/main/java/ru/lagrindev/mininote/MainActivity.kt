@file:OptIn(ExperimentalMaterial3Api::class)

package ru.lagrindev.mininote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.room.Room
import ru.lagrindev.mininote.ui.theme.MiniNoteTheme

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "notes.db"
        )
            .addMigrations(AppDatabase.MIGRATION_1_2)
            .build()
    }

    private val viewModel: NotesViewModel by viewModels {
        NotesVMFactory(NotesRepository(db.noteDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        // SplashScreen (Android 12+)
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setContent {

            val notes by viewModel.notes.collectAsState(initial = emptyList())

            var selectedTab by rememberSaveable { mutableStateOf(0) }

            // временно: системная тема
            val darkTheme = isSystemInDarkTheme()

            MiniNoteTheme(darkTheme = darkTheme) {

                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(
                                    text = if (selectedTab == 0)
                                        "Мини Заметки"
                                    else
                                        "Информация"
                                )
                            },
                            actions = {
                                Icon(
                                    imageVector = if (darkTheme)
                                        Icons.Default.DarkMode
                                    else
                                        Icons.Default.LightMode,
                                    contentDescription = null
                                )
                            }
                        )
                    },
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = selectedTab == 0,
                                onClick = { selectedTab = 0 },
                                icon = { Icon(Icons.Default.Home, null) },
                                label = { Text("Главная") }
                            )
                            NavigationBarItem(
                                selected = selectedTab == 1,
                                onClick = { selectedTab = 1 },
                                icon = { Icon(Icons.Default.Info, null) },
                                label = { Text("Инфо") }
                            )
                        }
                    }
                ) { innerPadding ->

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (selectedTab) {
                            0 -> NotesScreen(
                                notes = notes,
                                viewModel = viewModel
                            )
                            1 -> InfoScreen()
                        }
                    }
                }
            }
        }
    }
}
