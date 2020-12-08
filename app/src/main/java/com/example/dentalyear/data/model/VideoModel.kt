package com.example.dentalyear.data.model

import android.os.Parcelable
import com.example.dentalyear.utils.Utility
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
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
    var acf: VideoAcf,
    var downloadStatus: Int = Utility.NOT_DOWNLOADED,
    var downloaded: Long = 0,
    var totalSize: Long = 0,
) : Parcelable

@Parcelize
data class VideoAcf(
    @SerializedName("thumb_image") var thumbImage: String,
    @SerializedName("category")
    var videoCategories: List<VideoCategory>,
) : Parcelable

@Parcelize
data class VideoCategory(
    @SerializedName("ID")
    var id: Int,
    @SerializedName("post_date")
    var postDate: String,
    @SerializedName("post_title")
    var postTitle: String,
) : Parcelable