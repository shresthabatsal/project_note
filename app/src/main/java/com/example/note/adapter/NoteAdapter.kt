package com.example.note.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.note.R
import com.example.note.model.Note
import com.example.note.viewmodel.NoteViewModel

class NoteAdapter(
    private var notes: List<Note>,
    private val noteViewModel: NoteViewModel,
    private val onItemClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    // Update the notes list and notify the adapter
    fun updateNotes(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged() // Notify the adapter of data changes
    }

    fun getNoteAt(position: Int): Note {
        return notes[position]
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        private val textViewContent: TextView = itemView.findViewById(R.id.textViewContent)
        private val buttonFavorite: ImageButton = itemView.findViewById(R.id.buttonFavorite)

        fun bind(note: Note) {
            textViewTitle.text = note.title
            textViewContent.text = note.content

            // Set favorite icon based on status
            if (note.isFavorite) {
                buttonFavorite.setImageResource(R.drawable.baseline_star_24)
            } else {
                buttonFavorite.setImageResource(R.drawable.baseline_star_outline_24)
            }

            // Toggle favorite status on button click
            buttonFavorite.setOnClickListener {
                note.isFavorite = !note.isFavorite
                noteViewModel.updateNote(note) { success, message ->
                    if (success) {
                        // Update the icon
                        if (note.isFavorite) {
                            buttonFavorite.setImageResource(R.drawable.baseline_star_24)
                        } else {
                            buttonFavorite.setImageResource(R.drawable.baseline_star_outline_24)
                        }
                    }
                }
            }

            // Handle item click
            itemView.setOnClickListener {
                onItemClick(note)
            }
        }
    }
}