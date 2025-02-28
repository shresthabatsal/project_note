package com.example.note.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note.databinding.FragmentHomeBinding
import com.example.note.ui.activity.AddNoteActivity
import com.example.note.ui.activity.NoteDetailsActivity
import com.example.note.ui.adapter.NoteAdapter
import com.example.note.viewmodel.NoteViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val noteViewModel = NoteViewModel()
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the adapter
        noteAdapter = NoteAdapter(emptyList(), noteViewModel) { note ->
            val intent = Intent(requireContext(), NoteDetailsActivity::class.java)
            intent.putExtra("note", note)
            startActivity(intent)
        }

        // Set up RecyclerView
        binding.recyclerViewNotes.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewNotes.adapter = noteAdapter

        // Add button
        binding.buttonAddNote.setOnClickListener {
            val userId = noteViewModel.getCurrentUserId()
            if (userId.isEmpty()) return@setOnClickListener
            val intent = Intent(requireContext(), AddNoteActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        // Fetch and display notes
        val userId = noteViewModel.getCurrentUserId()
        if (userId.isNotEmpty()) {
            noteViewModel.getNotes(userId)
        }

        // Observe notes
        noteViewModel.notes.observe(viewLifecycleOwner) { notes ->
            noteAdapter.updateNotes(notes) // Update the adapter with the new list
        }
    }
    override fun onResume() {
        super.onResume()
        // Refresh the notes list when the fragment resumes
        val userId = noteViewModel.getCurrentUserId()
        if (userId.isNotEmpty()) {
            noteViewModel.getNotes(userId)
        }
    }
}