package com.example.dentalyear.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                videoLiveData = mainRepository.getVideos()
            }
        }
        return videoLiveData
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
        Log.d("HomeFragment", "Inside ViewModel")
        if (promptLiveData == null) {
            viewModelScope.launch {
                Log.d("HomeFragment", "Inside ViewModel2")
                mainRepository.getPrompts(filterKey)
            }
            promptLiveData = mainRepository.promptsLiveData
        }
        Log.d("HomeFragment", "Inside ViewModel3")
        return promptLiveData
    }

    fun refreshPrompts(filterKey: String){
        Log.d("HomeFragment", "Inside ViewModel Refresh")
        viewModelScope.launch {
            mainRepository.getPrompts(filterKey)
        }
    }
}