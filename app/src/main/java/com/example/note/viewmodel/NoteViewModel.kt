package com.example.note.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.note.model.Note
import com.example.note.repository.NoteRepository
import com.example.note.repository.NoteRepositoryImpl
import com.google.firebase.auth.FirebaseAuth

class NoteViewModel : ViewModel() {

    private val noteRepository: NoteRepository = NoteRepositoryImpl()
    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> get() = _notes

    // Get the current user's ID
    fun getCurrentUserId(): String {
        return FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    fun saveNote(note: Note, callback: (Boolean, String) -> Unit) {
        noteRepository.saveNote(note) { success, message ->
            if (success) {
                // Refresh the notes list after saving
                getNotes(note.userId)
            }
            callback(success, message)
        }
    }

    fun getNotes(userId: String) {
        noteRepository.getNotes(userId) { notes, success, _ ->
            if (success && notes != null) {
                _notes.value = notes
            }
        }
    }

    fun updateNote(note: Note, callback: (Boolean, String) -> Unit) {
        noteRepository.updateNote(note) { success, message ->
            if (success) {
                // Refresh the notes list after updating
                getNotes(note.userId)
            }
            callback(success, message)
        }
    }

    fun deleteNote(noteId: String, userId: String, callback: (Boolean, String) -> Unit) {
        noteRepository.deleteNote(noteId, userId) { success, message ->
            if (success) {
                // Refresh the notes list after deleting
                getNotes(userId)
            }
            callback(success, message)
        }
    }
}