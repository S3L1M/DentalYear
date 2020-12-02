package com.example.dentalyear.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dentalyear.data.api.DentalApi
import com.example.dentalyear.data.model.VideoModel
import com.example.dentalyear.utils.Resource
import com.example.dentalyear.utils.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainRepository {
    private var api: DentalApi
    private val videoLiveData = MutableLiveData<Resource<List<VideoModel>>>()

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(Utility.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(DentalApi::class.java)
    }

    // Get videos list | return liveData of videos
    fun getVideos(): LiveData<Resource<List<VideoModel>>> {
        // Send loading signal for the First call of the function
        videoLiveData.postValue(Resource.loading(null))

        api.getVideos().enqueue(object : Callback<List<VideoModel>> {
            override fun onResponse(
                call: Call<List<VideoModel>>,
                response: Response<List<VideoModel>>
            ) {
                // data is successfully loaded | send success signal
                if (response.body() != null) {
                    videoLiveData.postValue(Resource.success(response.body()))
                    Log.d("MainViewModel", "Everything is okay in Repo")
                    return
                }

                // Check if data is null
                videoLiveData.postValue(Resource.error(null, "data is null"))
                Log.d("MainViewModel", "Here From Repo")
            }

            override fun onFailure(call: Call<List<VideoModel>>, t: Throwable) {
                // data is failed to load | send error signal
                Log.d("MainViewModel", "Also here from Repo")
                videoLiveData.postValue(Resource.error(null, t.message.toString()))
            }
        })
        return videoLiveData
    }
}