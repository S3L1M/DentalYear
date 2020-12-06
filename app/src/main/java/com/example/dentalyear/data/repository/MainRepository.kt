package com.example.dentalyear.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.dentalyear.data.api.DentalApi
import com.example.dentalyear.data.database.HomeDao
import com.example.dentalyear.data.database.asDataModel
import com.example.dentalyear.data.database.asDatabaseModel
import com.example.dentalyear.data.model.HomeModel
import com.example.dentalyear.utils.Resource
import com.example.dentalyear.utils.Status
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val dentalApi: DentalApi,
    private val homeDao: HomeDao
) {

    val promptsLiveData = MutableLiveData<Resource<List<HomeModel>>>()

    // Get videos list | return liveData of videos
    suspend fun getVideos() = liveData {
        emit(Resource.loading(null))
        emit(dentalApi.getVideos())
    }

    // Get exhibits list
    suspend fun getExhibits() = liveData {
        emit(Resource.loading(null))
        emit(dentalApi.getExhibits())
    }

    // Get prompts list
    suspend fun getPrompts(filterKey: String) {
        promptsLiveData.postValue(Resource.loading(null))

        val source = homeDao.getPrompts(filterKey)

        if (source.isNullOrEmpty()) {
            val data = dentalApi.getPrompts(filterKey)
            if (data.status == Status.SUCCESS) {
                homeDao.insertPrompts(data.data!!.asDatabaseModel())
                promptsLiveData.postValue(data)
            } else {
                promptsLiveData.postValue(Resource.error(null, data.message.toString()))
            }
        } else {
            promptsLiveData.postValue(Resource.success(source.asDataModel()))
        }

    }
}