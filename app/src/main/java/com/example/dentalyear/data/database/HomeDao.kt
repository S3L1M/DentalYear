package com.example.dentalyear.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HomeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPrompts(home: List<HomeModelCached>)

    @Query("SELECT * FROM home_data_table WHERE promptDate = :date")
    suspend fun getPrompts(date: String): List<HomeModelCached>

    @Query("SELECT * FROM home_data_table")
    fun getPrompts(): LiveData<List<HomeModelCached>>

    @Query("SELECT * FROM home_data_table WHERE id = :id")
    fun getPrompt(id: Int): LiveData<List<HomeModelCached>>
}