package com.example.dentalyear.data.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class NoteModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val noteTitle: String,
    val noteContent: String,
    val noteDate: String,
    val noteType: Int,
) : Parcelable