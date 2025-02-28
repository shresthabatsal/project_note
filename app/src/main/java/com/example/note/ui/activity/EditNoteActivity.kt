package com.example.note.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.note.databinding.ActivityEditNoteBinding
import com.example.note.model.Note
import com.example.note.viewmodel.NoteViewModel

class EditNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditNoteBinding
    private lateinit var note: Note
    private val noteViewModel = NoteViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the note from the intent
        note = intent.getParcelableExtra("note") ?: return

        // Populate fields with note details
        binding.editTextTitle.setText(note.title)
        binding.editTextContent.setText(note.content)

        // Save button
        binding.buttonSave.setOnClickListener {
            val title = binding.editTextTitle.text.toString().trim()
            val content = binding.editTextContent.text.toString().trim()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Update the note
            note.title = title
            note.content = content

            // Save the updated note to the database
            noteViewModel.updateNote(note) { success, message ->
                if (success) {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

                    // Return the updated note to NoteDetailsActivity
                    val resultIntent = Intent()
                    resultIntent.putExtra("note", note)
                    setResult(RESULT_OK, resultIntent)

                    // Redirect to HomeFragment
                    val homeIntent = Intent(this, HomeActivity::class.java)
                    homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(homeIntent)
                    finish()
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Back button
        binding.buttonBack.setOnClickListener {
            onBackPressed()
        }
    }
}