package com.example.dentalyear.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dentalyear.data.model.VideoAcf
import com.example.dentalyear.data.model.VideoCategory
import com.example.dentalyear.utils.Utility

@Entity
data class VideoModelCached(
    @PrimaryKey
    val videoId: Int,
    val date: String,
    val videoTitle: String,
    val videoDuration: String,
    val link: String,
    val videoLink: String,
    val videoDownloadLink: String,
    val thumbImage: String,
    val categories: List<VideoCategory>,
    val downloadStatus: Int = Utility.NOT_DOWNLOADED,
    var downloaded: Long = 0,
    var totalSize: Long = 0,
) {
    fun getAcf(): VideoAcf {
        return VideoAcf(thumbImage, categories)
    }
}