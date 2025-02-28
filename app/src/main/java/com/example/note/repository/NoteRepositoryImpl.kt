package com.example.note.repository

import com.example.note.model.Note
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NoteRepositoryImpl : NoteRepository {

    private val database = FirebaseDatabase.getInstance().reference

    override fun saveNote(note: Note, callback: (Boolean, String) -> Unit) {
        val noteRef = database.child("notes").child(note.userId).push() // Generate a unique ID for the note
        note.id = noteRef.key ?: "" // Set the generated ID to the note
        noteRef.setValue(note)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Note saved successfully")
                } else {
                    callback(false, task.exception?.message ?: "Failed to save note")
                }
            }
    }

    override fun getNotes(userId: String, callback: (List<Note>?, Boolean, String) -> Unit) {
        database.child("notes").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val notes = mutableListOf<Note>()
                for (noteSnapshot in snapshot.children) {
                    val note = noteSnapshot.getValue(Note::class.java)
                    if (note != null) {
                        notes.add(note)
                    }
                }
                callback(notes, true, "Notes fetched successfully")
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }

    override fun updateNote(note: Note, callback: (Boolean, String) -> Unit) {
        database.child("notes").child(note.userId).child(note.id).setValue(note)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Note updated successfully")
                } else {
                    callback(false, task.exception?.message ?: "Failed to update note")
                }
            }
    }

    override fun deleteNote(noteId: String, userId: String, callback: (Boolean, String) -> Unit) {
        database.child("notes").child(userId).child(noteId).removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Note deleted successfully")
                } else {
                    callback(false, task.exception?.message ?: "Failed to delete note")
                }
            }
    }
}