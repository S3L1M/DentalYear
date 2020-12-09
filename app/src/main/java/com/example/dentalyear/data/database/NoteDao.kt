package com.example.dentalyear.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(note: NoteModel)

    @Update
    suspend fun updateNote(note: NoteModel)

    @Delete
    suspend fun deleteNote(note: NoteModel)

    @Query("SELECT * FROM notemodel WHERE id=:id LIMIT 1")
    suspend fun getNote(id: Int): NoteModel

    @Query("SELECT * FROM notemodel ORDER BY noteDate DESC")
    fun getAllNotes(): LiveData<List<NoteModel>>
}
