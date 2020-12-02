package com.example.dentalyear.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dentalyear.data.model.ExhibitModel
import com.example.dentalyear.data.model.VideoModel
import com.example.dentalyear.data.repository.MainRepository
import com.example.dentalyear.utils.Resource

class MainViewModel: ViewModel() {
    private val mainRepository = MainRepository()
    private lateinit var videoLiveData:LiveData<Resource<List<VideoModel>>>
    private lateinit var exhibitLiveData: LiveData<Resource<List<ExhibitModel>>>
//    private val isVideoLiveDataFetchedMap = MutableLiveData<Boolean>(false)


    fun getVideos(): LiveData<Resource<List<VideoModel>>>{
        // TODO: VERY IMPORTANT ADD SOME CONDITION SO THAT "getVideos()" DOESN'T GET CALLED EACH TIME
//        Log.d("MainViewModel", "Outside if statement")
//        if(isVideoLiveDataFetchedMap.value == false){
//            Log.d("MainViewModel", "Inside if statement")
            videoLiveData = mainRepository.getVideos()
//            isVideoLiveDataFetchedMap.postValue(true)
//        }
//        Log.d("MainViewModel", "Finally...")
        return videoLiveData
    }

    fun getExhibits(): LiveData<Resource<List<ExhibitModel>>>{
        exhibitLiveData = mainRepository.getExhibits()
        return exhibitLiveData
    }
}