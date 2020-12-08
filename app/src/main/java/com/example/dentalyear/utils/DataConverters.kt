package com.example.dentalyear.utils

import androidx.room.TypeConverter
import com.example.dentalyear.data.database.HomeModelCached
import com.example.dentalyear.data.database.VideoModelCached
import com.example.dentalyear.data.model.HomeModel
import com.example.dentalyear.data.model.VideoCategory
import com.example.dentalyear.data.model.VideoModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


fun List<HomeModel>.asHomeDatabaseModel(): List<HomeModelCached> {
    return map {
        HomeModelCached(
            it.id,
            it.link,
            it.promptCountry,
            it.promptDate,
            it.todaysFunHolidayTitle,
            it.acf.howToCelebrateDesc,
            it.acf.dailyMarketingTipDesc,
            it.acf.dailyPostsDesc,
            it.acf.howToMaximizePostDesc,
            it.acf.weeklyMarketingExercisesDesc,
            it.acf.marketingTrendsAndNewsForTheDayDesc,
            it.acf.adOfTheMonthDesc,
            it.acf.thisDateInHistoryDesc,
            it.acf.industryEventsDesc,
            it.acf.lookingAheadDesc
        )
    }
}

fun List<HomeModelCached>.asHomeDataModel(): List<HomeModel> {
    return map {
        HomeModel(
            it.id,
            it.link,
            it.promptCountry,
            it.promptDate,
            it.todaysFunHolidayTitle,
            it.getAcf(),
        )
    }
}


fun List<VideoModel>.asVideoDatabaseModel(): List<VideoModelCached> {
    return map {
        VideoModelCached(
            videoId = it.id,
            date = it.date,
            videoTitle = it.videoTitle,
            videoDuration = it.videoDuration,
            link = it.link,
            videoLink = it.videoLink,
            videoDownloadLink = it.videoDownloadLink,
            thumbImage = it.acf.thumbImage,
            categories = it.acf.videoCategories,
            downloadStatus = it.downloadStatus
        )
    }
}

fun List<VideoModelCached>.asVideoDataModel(): List<VideoModel> {
    return map {
        VideoModel(
            id = it.videoId,
            date = it.date,
            videoTitle = it.videoTitle,
            videoDuration = it.videoDuration,
            link = it.link,
            videoLink = it.videoLink,
            videoDownloadLink = it.videoDownloadLink,
            acf = it.getAcf(),
            downloadStatus = it.downloadStatus
        )
    }
}

fun VideoModel.asVideoDatabaseModel(): VideoModelCached {
    return VideoModelCached(
        videoId = this.id,
        videoTitle = this.videoTitle,
        date = this.date,
        videoDuration = this.videoDuration,
        videoDownloadLink = this.videoDownloadLink,
        link = this.link,
        videoLink = this.videoLink,
        thumbImage = this.acf.thumbImage,
        categories = this.acf.videoCategories,
        downloadStatus = this.downloadStatus,
        downloaded = downloaded,
        totalSize = totalSize,
    )
}


class DataConverter {
    @TypeConverter
    fun fromVideoCategoryList(category: List<VideoCategory?>?): String? {
        if (category == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<VideoCategory?>?>() {}.type
        return gson.toJson(category, type)
    }

    @TypeConverter
    fun toVideoCategoryList(categoryString: String?): List<VideoCategory>? {
        if (categoryString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<VideoCategory?>?>() {}.type
        return gson.fromJson<List<VideoCategory>>(categoryString, type)
    }
}