package com.example.note.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.note.databinding.FragmentFavoritesBinding
import com.example.note.model.Note
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

        // Add swipe-to-delete functionality
        setupSwipeToDelete(binding.recyclerViewFavorites)
    }

    private fun setupSwipeToDelete(recyclerView: RecyclerView) {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val noteToDelete = noteAdapter.getNoteAt(position)
                showDeleteConfirmationDialog(noteToDelete)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun showDeleteConfirmationDialog(note: Note) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Note")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Yes") { dialog, which ->
                // Delete the note using the ViewModel
                noteViewModel.deleteNote(note.id, note.userId) { success, message ->
                    if (success) {
                        // Note deleted successfully

                        Toast.makeText(requireContext(), "Note deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        // Handle deletion failure
                        Toast.makeText(requireContext(), "Failed to delete note: $message", Toast.LENGTH_SHORT).show()
                        noteAdapter.notifyDataSetChanged() // Refresh the adapter
                    }
                }
            }
            .setNegativeButton("No") { dialog, which ->
                // Notify the adapter to rebind the view holder
                noteAdapter.notifyDataSetChanged()
            }
            .setCancelable(false)
            .show()
    }
}