package com.example.dentalyear.data.api

import com.example.dentalyear.data.model.ExhibitModel
import com.example.dentalyear.data.model.VideoModel
import retrofit2.Call
import retrofit2.http.GET


interface DentalApi {
    @GET("video?per_page=100")
    fun getVideos(): Call<List<VideoModel>>

    @GET("sponsor")
    fun getSponsors(): Call<List<ExhibitModel>>
}