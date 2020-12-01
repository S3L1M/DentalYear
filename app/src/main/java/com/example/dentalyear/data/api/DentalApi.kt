package com.example.dentalyear.data.api

import com.example.dentalyear.data.model.VideoModel
import retrofit2.Call
import retrofit2.http.GET


interface DentalApi {
    @GET("video")
    fun getVideos(): Call<List<VideoModel>>
}