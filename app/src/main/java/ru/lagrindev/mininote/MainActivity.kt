package ru.lagrindev.mininote

import android.os.Bundle
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.room.*
import kotlinx.coroutines.launch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.layout.ContentScale

// --- Room Database Setup ---
@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val content: String
)

@Dao
interface NoteDao {
    @Insert
    suspend fun insert(note: Note)

    @Query("SELECT * FROM Note ORDER BY id DESC")
    suspend fun getAll(): List<Note>

    @Delete
    suspend fun delete(note: Note)
}

@Database(entities = [Note::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}

// --- MainActivity ---
@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "notes-db"
        ).build()

        setContent {
            var darkTheme by remember { mutableStateOf(true) }
            var noteText by remember { mutableStateOf(TextFieldValue("")) }
            var notes by remember { mutableStateOf(listOf<Note>()) }
            var selectedNoteId by remember { mutableStateOf<Int?>(null) }
            var selectedTab by remember { mutableStateOf(0) } // 0 = Home, 1 = Info
            val coroutineScope = rememberCoroutineScope()

            val colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()

            // Load notes from DB
            LaunchedEffect(Unit) {
                notes = db.noteDao().getAll()
            }

            MaterialTheme(colorScheme = colorScheme) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = if (selectedTab == 0) "Мини Заметки" else "Информация",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            },
                            actions = {
                                IconButton(onClick = { darkTheme = !darkTheme }) {
                                    Icon(
                                        imageVector = if (darkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                        contentDescription = "Switch Theme"
                                    )
                                }
                            }
                        )
                    },
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = selectedTab == 0,
                                onClick = { selectedTab = 0 },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Home,
                                        contentDescription = "Home",
                                        modifier = Modifier.size(30.dp)    // Размер иконки
                                    )
                                },
                                label = { Text("Главная") }
                            )

                            NavigationBarItem(
                                selected = selectedTab == 1,
                                onClick = { selectedTab = 1 },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Info",
                                        modifier = Modifier.size(30.dp)    // Размер иконки
                                    )
                                },
                                label = { Text("Инфо") }
                            )
                        }
                    }
                ) { paddingValues ->
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                    ) {
                        if (selectedTab == 0) {
                            // --- Home Screen ---
                            Column {
                                BasicTextField(
                                    value = noteText,
                                    onValueChange = { noteText = it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp)
                                        .background(Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                        .padding(8.dp)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        val content = noteText.text.trim()
                                        if (content.isNotEmpty()) {
                                            coroutineScope.launch {
                                                db.noteDao().insert(Note(content = content))
                                                notes = db.noteDao().getAll()
                                                noteText = TextFieldValue("")
                                            }
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Добавить заметку")
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                LazyColumn(modifier = Modifier.fillMaxSize()) {
                                    items(notes, key = { it.id }) { note ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp)
                                                .pointerInput(Unit) {
                                                    detectTapGestures(
                                                        onTap = {
                                                            selectedNoteId = if (selectedNoteId == note.id) null else note.id
                                                        }
                                                    )
                                                },
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                                            )
                                        ) {
                                            Column(modifier = Modifier.padding(12.dp)) {
                                                Text(text = note.content)

                                                if (selectedNoteId == note.id) {
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                    Button(
                                                        onClick = {
                                                            coroutineScope.launch {
                                                                db.noteDao().delete(note)
                                                                notes = db.noteDao().getAll()
                                                                selectedNoteId = null
                                                            }
                                                        },
                                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                                                    ) {
                                                        Text("Удалить", color = Color.White)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            // --- Info Screen ---
                            InfoScreen()
                        }
                    }
                }
            }
        }
    }
}

// --- InfoScreen ---
@Composable
fun InfoScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Ваше фото сверху ---
        Image(
            painter = painterResource(id = R.drawable.profile_photo),
            contentDescription = "Profile Photo",
            modifier = Modifier
                .size(256.dp)
                .padding(bottom = 16.dp)
                .clip(CircleShape)
        )

        Text("Мини Заметки v1.0", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(55.dp))

        // --- Горизонтальный ряд с иконками ---
        Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {

            // GitHub
            IconButton(onClick = { openUrl(context, "https://github.com/lagrindev") }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_github),
                    contentDescription = "GitHub",
                    modifier = Modifier.size(120.dp),
                    contentScale = ContentScale.Fit
                )
            }

            // Telegram
            IconButton(onClick = { openUrl(context, "https://t.me/username") }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_telegram),
                    contentDescription = "Telegram",
                    modifier = Modifier.size(120.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Spacer(modifier = Modifier.height(45.dp))
        Text("LagrinDev © 2025", color = Color.Gray)
    }
}

// --- Helper Function ---
fun openUrl(context: android.content.Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}
