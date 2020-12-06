package com.example.dentalyear.data.api

import com.example.dentalyear.data.model.ExhibitModel
import com.example.dentalyear.data.model.HomeModel
import com.example.dentalyear.data.model.VideoModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("video?per_page=100")
    suspend fun getVideos(): Response<List<VideoModel>>

    @GET("sponsor")
    suspend fun getSponsors(): Response<List<ExhibitModel>>

    @GET("prompt")
    suspend fun getPrompts(
        @Query("filter[meta_value]") filterValue: String,
        @Query("filter[meta_key]") filterKey: String = "prompt_date"
    ): Response<List<HomeModel>>
}