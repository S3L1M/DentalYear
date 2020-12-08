package com.example.dentalyear.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.dentalyear.data.api.DentalApi
import com.example.dentalyear.data.database.ApplicationDatabase
import com.example.dentalyear.data.database.VideoModelCached
import com.example.dentalyear.data.model.HomeModel
import com.example.dentalyear.data.model.VideoModel
import com.example.dentalyear.utils.*
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val dentalApi: DentalApi,
    private val database: ApplicationDatabase
) {

    val promptsLiveData = MutableLiveData<Resource<List<HomeModel>>>()
    val categoryWithVideosLiveData = MutableLiveData<Resource<List<VideoModel>>>()

    // Get videos list | return liveData of videos
    suspend fun getVideos() {
        Log.d("DownloadActivity", "HERE 5")
        val source = database.videoDao().getVideos()
        Log.d("DownloadActivity", "source: ${source.size}")

        if (source.isNullOrEmpty()) {
            Log.d("DownloadActivity", "HERE 6")
            categoryWithVideosLiveData.postValue(Resource.loading(null))
            val data = dentalApi.getVideos()
            if (data.status == Status.SUCCESS) {
                Log.d("DownloadActivity", "HERE 7")
                database.videoDao().insertVideos(data.data!!.asVideoDatabaseModel())
                categoryWithVideosLiveData.postValue(data)
            } else {
                Log.d("DownloadActivity", "HERE 8")
                categoryWithVideosLiveData.postValue(Resource.error(null, data.message.toString()))
            }
        } else {
            Log.d("DownloadActivity", "HERE 9")
            categoryWithVideosLiveData.postValue(Resource.success(source.asVideoDataModel()))
        }
    }

    suspend fun updateVideo(video: VideoModelCached){
        database.videoDao().updateVideo(video)
    }

    // Get exhibits list
    suspend fun getExhibits() = liveData {
        emit(Resource.loading(null))
        emit(dentalApi.getExhibits())
    }

    // Get prompts list
    suspend fun getPrompts(filterKey: String) {

        val source = database.homeDao().getPrompts(filterKey)

        if (source.isNullOrEmpty()) {
            promptsLiveData.postValue(Resource.loading(null))
            val data = dentalApi.getPrompts(filterKey)
            if (data.status == Status.SUCCESS) {
                database.homeDao().insertPrompts(data.data!!.asHomeDatabaseModel())
                promptsLiveData.postValue(data)
            } else {
                promptsLiveData.postValue(Resource.error(null, data.message.toString()))
            }
        } else {
            promptsLiveData.postValue(Resource.success(source.asHomeDataModel()))
        }

    }

}