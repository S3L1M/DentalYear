package com.example.dentalyear.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.dentalyear.data.api.DentalApi
import com.example.dentalyear.data.database.ApplicationDatabase
import com.example.dentalyear.data.database.HomeModelCached
import com.example.dentalyear.data.database.asDataModel
import com.example.dentalyear.data.database.asDatabaseModel
import com.example.dentalyear.data.model.ExhibitModel
import com.example.dentalyear.data.model.HomeModel
import com.example.dentalyear.data.model.VideoModel
import com.example.dentalyear.utils.Resource
import com.example.dentalyear.utils.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainRepository(private val database: ApplicationDatabase) {
    private var api: DentalApi
    private val videoLiveData = MutableLiveData<Resource<List<VideoModel>>>()
    private val exhibitLiveData = MutableLiveData<Resource<List<ExhibitModel>>>()

        private val promptLiveData = MutableLiveData<Resource<List<HomeModel>>>()
//    private val promptLiveData: LiveData<Resource<List<HomeModel>>> = Transformations.map(database.homeDao().getPrompts("")){
//        Resource.success(it.asDataModel())
//    }

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
                    return
                }

                // data is null
                videoLiveData.postValue(Resource.error(null, "data is null"))
            }

            override fun onFailure(call: Call<List<VideoModel>>, t: Throwable) {
                // data is failed to load | send error signal
                videoLiveData.postValue(Resource.error(null, t.message.toString()))
            }
        })
        return videoLiveData
    }

    // Get exhibits list
    fun getExhibits(): LiveData<Resource<List<ExhibitModel>>> {
        exhibitLiveData.postValue(Resource.loading(null))

        api.getSponsors().enqueue(object : Callback<List<ExhibitModel>> {
            override fun onResponse(
                call: Call<List<ExhibitModel>>,
                response: Response<List<ExhibitModel>>
            ) {
                // data is successfully loaded | send success signal
                if (response.body() != null) {
                    exhibitLiveData.postValue(Resource.success(response.body()))
                    return
                }

                // data is null
                exhibitLiveData.postValue(Resource.error(null, "data is null"))
            }

            override fun onFailure(call: Call<List<ExhibitModel>>, t: Throwable) {
                exhibitLiveData.postValue(Resource.error(null, t.message.toString()))
            }
        })
        return exhibitLiveData
    }

    // Get prompts list
    suspend fun getPrompts(filterKey: String): LiveData<Resource<List<HomeModel>>> {
        Log.d("HomeFragment", "Inside Repository")
        promptLiveData.postValue(Resource.loading(null))
        val prompts = database.homeDao().getPrompts(filterKey)
        if (prompts.isNotEmpty()) {
            Log.d("HomeFragment", "Inside Repository2")
            promptLiveData.postValue(Resource.success(prompts.asDataModel()))
        } else {
            try {
                Log.d("HomeFragment", "Inside Repository3")
                val prom = api.getPrompts(filterKey).asDatabaseModel()
                database.homeDao().insertPrompts(prom)
                promptLiveData.postValue(Resource.success(prom.asDataModel()))
            } catch (cause: Throwable) {
                Log.d("HomeFragment", "Inside Repository4")
                promptLiveData.postValue(Resource.error(null, cause.message.toString()))
            }
        }

//        api.getPrompts(filterKey).enqueue(object: Callback<List<HomeModel>>{
//            override fun onResponse(
//                call: Call<List<HomeModel>>,
//                response: Response<List<HomeModel>>
//            ) {
//                // data is successfully loaded | send success signal
//                if (response.body() != null) {
//                    promptLiveData.postValue(Resource.success(response.body()))
//                    return
//                }
//
//                // data is null
//                promptLiveData.postValue(Resource.error(null, "data is null"))
//            }
//
//            override fun onFailure(call: Call<List<HomeModel>>, t: Throwable) {
//                promptLiveData.postValue(Resource.error(null, t.message.toString()))
//            }
//        })
        Log.d("HomeFragment", "Inside Repository5")
        Log.d("HomeFragment", "${prompts.size}")
        Log.d("HomeFragment", "${prompts.asDataModel().size}")
        return promptLiveData
    }

    // Insert Prompts to database
}