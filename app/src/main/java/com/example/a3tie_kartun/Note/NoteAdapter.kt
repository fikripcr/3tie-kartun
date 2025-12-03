package com.example.a3tie_kartun.Note

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a3tie_kartun.Data.Entity.NoteEntity
import com.example.a3tie_kartun.databinding.ItemNoteBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class NoteAdapter(
    private val notes: List<NoteEntity>,
    private val noteFragment: NoteFragment
) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.binding.tvTitle.text = note.title
        holder.binding.tvContent.text = note.content

        holder.itemView.setOnClickListener {
            Snackbar.make(holder.itemView, "Clicked: ${note.title}", Snackbar.LENGTH_SHORT).show()
        }
        holder.binding.btnDelete.setOnClickListener {
            MaterialAlertDialogBuilder(holder.itemView.context)
                .setTitle("Hapus Catatan")
                .setMessage("Apakah kamu yakin ingin menghapus catatan ini?")
                .setPositiveButton("Ya") { dialog, _ ->

                    noteFragment.deleteNote(note)

                    dialog.dismiss()

                }
                .setNegativeButton("Batal") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    override fun getItemCount(): Int = notes.size
}