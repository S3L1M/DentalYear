package com.example.dentalyear.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.dentalyear.R
import com.example.dentalyear.data.database.NoteModel
import com.example.dentalyear.utils.Utility
import com.example.dentalyear.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_add_note.*

@AndroidEntryPoint
class AddNoteActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        // Define view model
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val note: NoteModel = intent.extras?.getParcelable(NoteFragment.NOTE_DATA)!!
        val type = intent.extras?.getInt(NoteFragment.MODE_TYPE)

        // Set date to the header
        activity_add_note_note_date_image_view.text = note.noteDate
        // Check if the user in editing mode | the user clicked the note
        if (type == Utility.NOTE_EDITING_MODE) {
            activity_add_note_title_edit_text.setText(note.noteTitle)
            activity_add_note_content_edit_text.setText(note.noteContent)
        }

        // Listen for the save image view
        activity_add_note_save_image_view.setOnClickListener {
            val title = activity_add_note_title_edit_text.text.toString()
            val content = activity_add_note_content_edit_text.text.toString()
            if (
                title.isNotEmpty() &&
                content.isNotEmpty() &&
                note.noteDate.isNotEmpty() &&
                note.noteType != -1
            ) {
                val newNote = NoteModel(
                    noteTitle = title,
                    noteContent = content,
                    noteDate = note.noteDate,
                    noteType = note.noteType
                )
                when (type) {
                    Utility.NOTE_CREATING_MODE -> saveNote(newNote)
                    Utility.NOTE_EDITING_MODE -> {
                        newNote.id = note.id
                        updateNote(newNote)
                    }
                }
                finish()
            } else {
                Toast.makeText(this, "title and content can't be empty", Toast.LENGTH_SHORT).show()
            }
        }

        // Listen for back button
        activity_add_note_back_image_view.setOnClickListener {
            finish()
        }
    }

    private fun saveNote(note: NoteModel) {
        viewModel.insertNote(note)
    }

    private fun updateNote(note: NoteModel) {
        Log.d("NoteFragment", "Update Note: $title")
        viewModel.updateNote(note)
    }
}