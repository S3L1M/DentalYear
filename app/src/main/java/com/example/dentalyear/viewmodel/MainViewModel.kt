package com.example.dentalyear.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dentalyear.data.database.VideoModelCached
import com.example.dentalyear.data.model.ExhibitModel
import com.example.dentalyear.data.model.HomeModel
import com.example.dentalyear.data.model.VideoModel
import com.example.dentalyear.data.repository.MainRepository
import com.example.dentalyear.utils.Resource
import kotlinx.coroutines.launch


class MainViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    private var videoLiveData: LiveData<Resource<List<VideoModel>>>? = null
    private var exhibitLiveData: LiveData<Resource<List<ExhibitModel>>>? = null
    private var promptLiveData: LiveData<Resource<List<HomeModel>>>? = null


    fun getVideos(): LiveData<Resource<List<VideoModel>>>? {
        if (videoLiveData == null) {
            viewModelScope.launch {
                mainRepository.getVideos()
            }
            videoLiveData = mainRepository.categoryWithVideosLiveData
        }
        return videoLiveData
    }

    fun updateVideo(video: VideoModelCached){
        viewModelScope.launch {
            mainRepository.updateVideo(video)
//            mainRepository.getVideos()
        }
    }

    fun refreshVideos(){
        viewModelScope.launch {
            mainRepository.getVideos()
        }
    }

    fun getExhibits(): LiveData<Resource<List<ExhibitModel>>>? {
        if (exhibitLiveData == null) {
            viewModelScope.launch {
                exhibitLiveData = mainRepository.getExhibits()
            }
        }
        return exhibitLiveData
    }

    fun getPrompts(filterKey: String): LiveData<Resource<List<HomeModel>>>? {
        if (promptLiveData == null) {
            viewModelScope.launch {
                mainRepository.getPrompts(filterKey)
            }
            promptLiveData = mainRepository.promptsLiveData
        }
        return promptLiveData
    }

    fun refreshPrompts(filterKey: String) {
        viewModelScope.launch {
            mainRepository.getPrompts(filterKey)
        }
    }
}