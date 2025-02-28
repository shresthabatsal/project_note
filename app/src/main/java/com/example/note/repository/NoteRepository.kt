package com.example.note.repository

import com.example.note.model.Note

interface NoteRepository {
    fun saveNote(note: Note, callback: (Boolean, String) -> Unit)
    fun getNotes(userId: String, callback: (List<Note>?, Boolean, String) -> Unit)
    fun updateNote(note: Note, callback: (Boolean, String) -> Unit)
    fun deleteNote(noteId: String, userId: String, callback: (Boolean, String) -> Unit)
}