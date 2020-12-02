package com.example.dentalyear.data.model

import com.google.gson.annotations.SerializedName

data class VideoModel(
    var id: Int,
    var date: String,
    var link: String,
    @SerializedName("video_title")
    var videoTitle: String,
    @SerializedName("video_link")
    var videoLink: String,
    @SerializedName("download_video_link")
    var videoDownloadLink: String,
    @SerializedName("video_duration")
    var videoDuration: String,
    var acf: VideoAcf
)

data class VideoAcf(@SerializedName("thumb_image")var thumbImage:String)