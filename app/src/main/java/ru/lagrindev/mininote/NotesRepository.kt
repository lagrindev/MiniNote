package ru.lagrindev.mininote

import kotlinx.coroutines.flow.Flow

class NotesRepository(private val dao: NoteDao) {

    fun getNotes(): Flow<List<Note>> = dao.getAllNotes()

    suspend fun addNote(note: Note) = dao.insert(note)

    suspend fun updateNote(note: Note) = dao.update(note)

    suspend fun deleteNote(note: Note) = dao.delete(note)
}
