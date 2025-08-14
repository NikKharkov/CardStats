package com.example.cardstats.notes.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardstats.notes.data.NoteEntity
import com.example.cardstats.notes.data.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter

class NoteViewModel(
    private val noteRepository: NoteRepository
) : ViewModel() {
    val allNotes: Flow<List<NoteEntity>> = noteRepository.getAllNotes()

    fun saveNote(title: String, date: String, gameId: Int, description: String) {
        viewModelScope.launch {
            val note = NoteEntity(
                title = title,
                date = date,
                gameId = gameId,
                description = description,
                isIconFilled = false
            )
            noteRepository.insertNote(note)
        }
    }

    fun updateNote(id: Int, title: String, date: String, gameId: Int, description: String) {
        viewModelScope.launch {
            val note = noteRepository.getNoteById(id)
            if (note != null) {
                val updatedNote = note.copy(
                    title = title,
                    date = date,
                    gameId = gameId,
                    description = description
                )
                noteRepository.updateNote(updatedNote)
            }
        }
    }

    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            noteRepository.deleteNote(note)
        }
    }

    fun formatDate(localDate: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return localDate.toJavaLocalDate().format(formatter)
    }

    fun updateIconFilled(noteId: Int, isIconFilled: Boolean) {
        viewModelScope.launch {
            val note = noteRepository.getNoteById(noteId)
            if (note != null) {
                val updatedNote = note.copy(isIconFilled = isIconFilled)
                noteRepository.updateNote(updatedNote)
                Log.d("NoteViewModel", "Updated note $noteId with isIconFilled = $isIconFilled")
            } else {
                Log.e("NoteViewModel", "Note with ID $noteId not found")
            }
        }
    }

    suspend fun getNoteById(id: Int): NoteEntity? = noteRepository.getNoteById(id)

}