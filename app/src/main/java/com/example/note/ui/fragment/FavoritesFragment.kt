package com.example.note.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note.databinding.FragmentFavoritesBinding
import com.example.note.ui.adapter.NoteAdapter
import com.example.note.viewmodel.NoteViewModel

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private val noteViewModel = NoteViewModel()
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the adapter
        noteAdapter = NoteAdapter(emptyList(), noteViewModel) { note ->
            // Handle item click (optional)
        }

        // Set up RecyclerView
        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFavorites.adapter = noteAdapter

        // Fetch and display favorite notes
        val userId = noteViewModel.getCurrentUserId()
        if (userId.isNotEmpty()) {
            noteViewModel.getNotes(userId)
        }

        // Observe notes and filter favorites
        noteViewModel.notes.observe(viewLifecycleOwner) { notes ->
            val favoriteNotes = notes.filter { it.isFavorite } // Filter notes with isFavorite = true
            noteAdapter.updateNotes(favoriteNotes)
        }
    }
}