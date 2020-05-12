package com.morcinek.omise.ui.donation

import retrofit2.http.Body
import retrofit2.http.POST

data class DonationRequest(
    val amount: Int,
    val name: String,
    val token: String
)

data class DonationResponse(
    val success: Boolean,
    val error_code: String,
    val error_message: String
)

interface DonationsApi {
    @POST("donations")
    suspend fun postData(@Body request: DonationRequest): DonationResponse
}

