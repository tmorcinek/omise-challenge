package com.morcinek.omise.ui.charities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import retrofit2.http.GET

data class CharitiesData(
    val data: List<CharityData>
)

@Parcelize
data class CharityData(
    val id: Int,
    val name: String,
    val logo_url: String
) : Parcelable

interface CharitiesApi {
    @GET("charities")
    suspend fun getData(): CharitiesData
}