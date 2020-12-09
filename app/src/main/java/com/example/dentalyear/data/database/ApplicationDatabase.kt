package com.example.dentalyear.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dentalyear.utils.DataConverter

@Database(entities = [HomeModelCached::class, VideoModelCached::class, NoteModel::class], version = 1, exportSchema = true)
@TypeConverters(DataConverter::class)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun homeDao(): HomeDao
    abstract fun videoDao(): VideoDao
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: ApplicationDatabase? = null

        fun getDatabase(context: Context): ApplicationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ApplicationDatabase::class.java,
                    "dental_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}