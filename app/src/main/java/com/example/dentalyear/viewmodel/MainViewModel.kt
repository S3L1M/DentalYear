package com.example.dentalyear.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dentalyear.data.model.VideoModel
import com.example.dentalyear.data.repository.MainRepository
import com.example.dentalyear.utils.Resource

class MainViewModel: ViewModel() {
    private val mainRepository = MainRepository()
    private lateinit var videoLiveData:LiveData<Resource<List<VideoModel>>>
    private var isVideoDataFetched = false

    fun getVideos(): LiveData<Resource<List<VideoModel>>>{
        if(!isVideoDataFetched){
            videoLiveData = mainRepository.getVideos()
            isVideoDataFetched = true
        }
        return videoLiveData
    }
}