package com.example.note.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.note.databinding.ActivityNoteDetailsBinding
import com.example.note.model.Note

class NoteDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteDetailsBinding
    private lateinit var note: Note

    // Register for result from EditNoteActivity
    private val editNoteLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // Refresh the note details
            note = result.data?.getParcelableExtra("note") ?: return@registerForActivityResult
            binding.textViewTitle.text = note.title
            binding.textViewContent.text = note.content

            // Finish the activity to return to HomeFragment
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the note from the intent
        note = intent.getParcelableExtra("note") ?: return

        // Display note details
        binding.textViewTitle.text = note.title
        binding.textViewContent.text = note.content

        // Edit button
        binding.buttonEdit.setOnClickListener {
            val intent = Intent(this, EditNoteActivity::class.java)
            intent.putExtra("note", note)
            editNoteLauncher.launch(intent)
        }

        // Back button
        binding.buttonBack.setOnClickListener {
            onBackPressed()
        }
    }
}