package com.example.note.model

data class Note(
    var id: String = "", // Unique ID for the note
    val userId: String = "", // ID of the logged-in user
    val title: String = "",
    val content: String = "",
    val isFavorite: Boolean = false // Default status is false
)