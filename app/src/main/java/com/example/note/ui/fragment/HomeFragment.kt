package com.example.note.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note.R
import com.example.note.databinding.FragmentHomeBinding
import com.example.note.ui.activity.AddNoteActivity
import com.example.note.viewmodel.NoteViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val noteViewModel = NoteViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Add button
        binding.buttonAddNote.setOnClickListener {
            val userId = noteViewModel.getCurrentUserId() // Get the current user's ID
            if (userId.isEmpty()) {
                // Handle case where user is not logged in
                return@setOnClickListener
            }
            val intent = Intent(requireContext(), AddNoteActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        // Fetch and display notes
        val userId = noteViewModel.getCurrentUserId() // Get the current user's ID
        if (userId.isNotEmpty()) {
            noteViewModel.getNotes(userId)
        }

        // Observe notes
//        noteViewModel.notes.observe(viewLifecycleOwner) { notes ->
//            // Update RecyclerView or UI with the notes
//            val adapter = NoteAdapter(notes) // Replace with your adapter
//            binding.recyclerViewNotes.layoutManager = LinearLayoutManager(requireContext())
//            binding.recyclerViewNotes.adapter = adapter
//        }
    }
}