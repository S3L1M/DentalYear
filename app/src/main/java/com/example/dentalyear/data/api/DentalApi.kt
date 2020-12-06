package com.example.dentalyear.data.api

import javax.inject.Inject

class DentalApi @Inject constructor(
    private val apiService: ApiService
) : ApiHelper() {
    suspend fun getPrompts(filterKey: String) = getResult { apiService.getPrompts(filterKey) }
    suspend fun getExhibits() = getResult { apiService.getSponsors() }
    suspend fun getVideos() = getResult { apiService.getVideos() }
}