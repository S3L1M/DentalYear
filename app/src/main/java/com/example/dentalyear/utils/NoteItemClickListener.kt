package com.example.dentalyear.utils

import com.example.dentalyear.data.database.NoteModel

interface NoteItemClickListener {
    fun noteItemClicked(note: NoteModel)
}