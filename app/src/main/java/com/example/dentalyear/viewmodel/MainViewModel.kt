package com.example.dentalyear.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.dentalyear.data.database.ApplicationDatabase
import com.example.dentalyear.data.model.ExhibitModel
import com.example.dentalyear.data.model.HomeModel
import com.example.dentalyear.data.model.VideoModel
import com.example.dentalyear.data.repository.MainRepository
import com.example.dentalyear.utils.Resource
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val mainRepository = MainRepository(ApplicationDatabase.getDatabase(application))
    private var videoLiveData: LiveData<Resource<List<VideoModel>>>? = null
    private var exhibitLiveData: LiveData<Resource<List<ExhibitModel>>>? = null
    private var promptLiveData: LiveData<Resource<List<HomeModel>>>? = null


    fun getVideos(): LiveData<Resource<List<VideoModel>>>? {
        if (videoLiveData == null)
            videoLiveData = mainRepository.getVideos()
        return videoLiveData
    }

    fun getExhibits(): LiveData<Resource<List<ExhibitModel>>>? {
        if (exhibitLiveData == null)
            exhibitLiveData = mainRepository.getExhibits()
        return exhibitLiveData
    }

    fun getPrompts(filterKey: String): LiveData<Resource<List<HomeModel>>>? {
        Log.d("HomeFragment", "Inside ViewModel")
        viewModelScope.launch {
            Log.d("HomeFragment", "Inside ViewModel2")
            promptLiveData = mainRepository.getPrompts(filterKey)
        }
        Log.d("HomeFragment", "Inside ViewModel3")
        return promptLiveData
    }
}