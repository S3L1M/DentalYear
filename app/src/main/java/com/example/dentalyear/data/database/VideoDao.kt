package com.example.dentalyear.data.database

import androidx.room.*

@Dao
abstract class VideoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertVideo(video: VideoModelCached)

    @Update
    abstract suspend fun updateVideo(video: VideoModelCached)

    @Insert
    abstract suspend fun insertVideos(videos: List<VideoModelCached>)

    @Query("SELECT * FROM VideoModelCached")
    abstract suspend fun getVideos(): List<VideoModelCached>
}