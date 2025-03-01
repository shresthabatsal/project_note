package com.example.note.ui.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.note.databinding.FragmentHomeBinding
import com.example.note.model.Note
import com.example.note.ui.activity.AddNoteActivity
import com.example.note.ui.activity.NoteDetailsActivity
import com.example.note.ui.adapter.NoteAdapter
import com.example.note.viewmodel.NoteViewModel
import com.example.note.viewmodel.UserViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val noteViewModel = NoteViewModel()
    private var userViewModel = UserViewModel()
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

        // Fetch and display user data
        val currentUser = userViewModel.getCurrentUser()
        if (currentUser != null) {
            userViewModel.fetchUserData(currentUser.uid)
        }

        userViewModel.userData.observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                binding.welcomeText.text = "Hello! ${user.fullName}"
            } else {
                binding.welcomeText.text = "Hello!"
            }
        })

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

        // Add swipe-to-delete functionality
        setupSwipeToDelete(binding.recyclerViewNotes)
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

    override fun onResume() {
        super.onResume()
        // Refresh the notes list when the fragment resumes
        val userId = noteViewModel.getCurrentUserId()
        if (userId.isNotEmpty()) {
            noteViewModel.getNotes(userId)
        }
    }
}