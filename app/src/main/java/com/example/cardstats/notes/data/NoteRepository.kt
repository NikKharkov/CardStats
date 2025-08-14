package com.example.cardstats.notes.data

import android.util.Log
import kotlinx.coroutines.flow.Flow

class NoteRepository(
    private val noteDao: NoteDao
) {
    fun getAllNotes(): Flow<List<NoteEntity>> = noteDao.getAllNotes()

    suspend fun getNoteById(id: Int): NoteEntity? {
        val note = noteDao.getNoteById(id)
        Log.d("NoteRepository", "Fetched note with ID $id: $note")
        return note
    }

    fun getNotesByGameId(gameId: Int): Flow<List<NoteEntity>> = noteDao.getNotesByGameId(gameId)

    suspend fun insertNote(note: NoteEntity) {
        Log.d("NoteRepository", "Inserting note: $note")
        noteDao.insertNote(note)
    }

    suspend fun updateNote(note: NoteEntity) {
        Log.d("NoteRepository", "Updating note: $note")
        noteDao.updateNote(note)
    }

    suspend fun deleteNote(note: NoteEntity) {
        Log.d("NoteRepository", "Deleting note: $note")
        noteDao.deleteNote(note)
    }
}