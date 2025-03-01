package com.example.note.ui.fragment

import android.R
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.note.databinding.FragmentSearchBinding
import com.example.note.model.Note
import com.example.note.ui.adapter.NoteAdapter
import com.example.note.viewmodel.NoteViewModel


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val noteViewModel = NoteViewModel()
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the adapter
        noteAdapter = NoteAdapter(emptyList(), noteViewModel) { note ->
            // Handle item click (optional)
        }

        val searchView: androidx.appcompat.widget.SearchView = binding.searchView
        searchView.setIconified(false) // Keeps SearchView always open
        searchView.clearFocus() // Prevents keyboard from automatically opening

        // Set up RecyclerView
        binding.recyclerViewSearchResults.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSearchResults.adapter = noteAdapter

        // Fetch and display all notes
        val userId = noteViewModel.getCurrentUserId()
        if (userId.isNotEmpty()) {
            noteViewModel.getNotes(userId)
        }

        // Observe notes and filter based on search query
        noteViewModel.notes.observe(viewLifecycleOwner) { notes ->
            val query = binding.searchView.query.toString().trim()
            val searchResults = if (query.isEmpty()) {
                emptyList() // Show no results if the query is empty
            } else {
                notes.filter { note ->
                    note.title.contains(query, ignoreCase = true) || note.content.contains(query, ignoreCase = true)
                }
            }
            noteAdapter.updateNotes(searchResults)
        }

        // Set up search functionality
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Trigger search when the user submits the query
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Trigger search as the user types
                noteViewModel.notes.value?.let { notes ->
                    val query = newText?.trim() ?: ""
                    val searchResults = if (query.isEmpty()) {
                        emptyList() // Show no results if the query is empty
                    } else {
                        notes.filter { note ->
                            note.title.contains(query, ignoreCase = true) || note.content.contains(query, ignoreCase = true)
                        }
                    }
                    noteAdapter.updateNotes(searchResults)
                }
                return true
            }
        })

        // Add swipe-to-delete functionality
        setupSwipeToDelete(binding.recyclerViewSearchResults)
    }

    private fun setupSwipeToDelete(recyclerView: RecyclerView) {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
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