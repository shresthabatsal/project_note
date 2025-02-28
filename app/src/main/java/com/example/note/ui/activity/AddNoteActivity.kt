package com.example.note.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.note.databinding.ActivityAddNoteBinding
import com.example.note.model.Note
import com.example.note.viewmodel.NoteViewModel

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private val noteViewModel = NoteViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button
        binding.buttonBack.setOnClickListener {
            finish() // Close the activity
        }

        // Add button
        binding.buttonAdd.setOnClickListener {
            val title = binding.editTextTitle.text.toString().trim()
            val content = binding.editTextContent.text.toString().trim()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Get the logged-in user's ID
            val userId = intent.getStringExtra("userId") ?: ""
            if (userId.isEmpty()) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create a new note
            val note = Note(
                userId = userId,
                title = title,
                content = content,
                isFavorite = false // Default status
            )

            // Save the note to the database
            noteViewModel.saveNote(note) { success, message ->
                if (success) {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    finish() // Close the activity after saving
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}