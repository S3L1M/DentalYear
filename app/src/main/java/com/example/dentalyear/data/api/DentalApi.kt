package com.example.dentalyear.data.api

import com.example.dentalyear.data.model.ExhibitModel
import com.example.dentalyear.data.model.HomeModel
import com.example.dentalyear.data.model.VideoModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface DentalApi {
    @GET("video?per_page=100")
    fun getVideos(): Call<List<VideoModel>>

    @GET("sponsor")
    fun getSponsors(): Call<List<ExhibitModel>>

    @GET("prompt")
    fun getPrompts(
        @Query("filter[meta_value]") filterValue: String,
        @Query("filter[meta_key]") filterKey: String = "prompt_date"
    ): Call<List<HomeModel>>
}