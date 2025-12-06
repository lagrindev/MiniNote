package ru.lagrindev.mininote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.room.*
import kotlinx.coroutines.launch
import androidx.compose.material3.ExperimentalMaterial3Api

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
            val coroutineScope = rememberCoroutineScope()

            // Load notes from DB
            LaunchedEffect(Unit) {
                notes = db.noteDao().getAll()
            }

            MaterialTheme(
                colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()
            ) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Мини Заметки",
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
                    }
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(16.dp)
                    ) {
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

                        var selectedNoteId by remember { mutableStateOf<Int?>(null) }

                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(notes, key = { it.id }) { note ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onTap = {
                                                    // Выбираем заметку при нажатии
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

                                        // Кнопка удаления для выбранной заметки
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
                }
            }
        }
    }
}
