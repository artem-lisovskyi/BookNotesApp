package com.booknotes.booknotesapp.network

import com.booknotes.booknotesapp.network.model.BookNames
import com.booknotes.booknotesapp.network.model.RecommendationResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface RecommendationApi {
    @Headers("Content-Type: application/json")
    @POST("recommend")
    fun getRecommendations(@Body bookNames: BookNames): Call<RecommendationResponse>
}
