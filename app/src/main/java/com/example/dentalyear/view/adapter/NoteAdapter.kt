package com.example.dentalyear.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dentalyear.R
import com.example.dentalyear.data.database.NoteModel
import com.example.dentalyear.utils.NoteItemClickListener

class NoteAdapter(
    private val itemClick: NoteItemClickListener,
    private var data: List<NoteModel>? = null
) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NoteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_row_note, parent, false)
        )

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = data?.get(position)
        holder.noteTitle.text = note?.noteTitle
        holder.noteDate.text = note?.noteDate
        holder.itemView.setOnClickListener {
            itemClick.noteItemClicked(note!!)
        }
    }

    override fun getItemCount() = data?.size ?: 0

    fun setData(data: List<NoteModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteTitle: TextView =
            itemView.findViewById(R.id.note_recycler_view_note_title)

        val noteDate: TextView =
            itemView.findViewById(R.id.note_recycler_view_note_date)

        val noteEditImageView: ImageView =
            itemView.findViewById(R.id.note_recycler_view_note_edit_image_view)
    }
}