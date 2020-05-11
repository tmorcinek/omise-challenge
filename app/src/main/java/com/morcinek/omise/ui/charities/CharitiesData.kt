package com.morcinek.omise.ui.charities

import retrofit2.http.GET

data class CharitiesData(
    val data: List<CharityData>
)

data class CharityData(
    val id: Int,
    val name: String,
    val logo_url: String
)

interface CharitiesApi {
    @GET("charities")
    suspend fun getData(): CharitiesData
}