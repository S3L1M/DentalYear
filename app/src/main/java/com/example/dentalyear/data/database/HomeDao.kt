package com.example.dentalyear.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HomeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPrompts(home: List<HomeModelCached>)

    @Query("SELECT * FROM home_data_table WHERE promptDate = :date")
    suspend fun getPrompts(date: String): List<HomeModelCached>

    @Query("SELECT * FROM home_data_table WHERE id = :id")
    suspend fun getPrompt(id: Int): List<HomeModelCached>
}