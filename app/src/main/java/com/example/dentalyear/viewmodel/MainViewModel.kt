package com.example.dentalyear.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.dentalyear.data.model.ExhibitModel
import com.example.dentalyear.data.model.VideoModel
import com.example.dentalyear.data.repository.MainRepository
import com.example.dentalyear.utils.Resource

class MainViewModel : ViewModel() {
    private val mainRepository = MainRepository()
    private var videoLiveData: LiveData<Resource<List<VideoModel>>>?= null
    private var exhibitLiveData: LiveData<Resource<List<ExhibitModel>>>?=null


    fun getVideos(): LiveData<Resource<List<VideoModel>>> {
        if(videoLiveData == null)
            videoLiveData = mainRepository.getVideos()
        return videoLiveData as LiveData<Resource<List<VideoModel>>>
    }

    fun getExhibits(): LiveData<Resource<List<ExhibitModel>>> {
        if(exhibitLiveData == null)
            exhibitLiveData = mainRepository.getExhibits()
        return exhibitLiveData as LiveData<Resource<List<ExhibitModel>>>
    }
}