package ru.lagrindev.mininote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NotesRepository) : ViewModel() {

    val notes: StateFlow<List<Note>> = repository.getNotes()
        .map { it }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addNote(text: String) {
        viewModelScope.launch {
            repository.addNote(Note(text = text))
        }
    }

    fun updateNote(note: Note, newText: String) {
        viewModelScope.launch {
            repository.updateNote(note.copy(text = newText))
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }
}

class NotesVMFactory(private val repository: NotesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
