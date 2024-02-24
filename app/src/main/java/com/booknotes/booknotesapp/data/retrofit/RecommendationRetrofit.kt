package com.booknotes.booknotesapp.data.retrofit

import com.booknotes.booknotesapp.network.RecommendationApi
import com.booknotes.booknotesapp.network.model.BookNames
import com.booknotes.booknotesapp.network.model.RecommendationResponse
import retrofit2.Call

interface RecommendationRetrofit {
    suspend fun getRecommendations(bookNames: BookNames): Call<RecommendationResponse>
}

class NetworkRecommendationRepository(
    private val recommendApi: RecommendationApi
) : RecommendationRetrofit {

    override suspend fun getRecommendations(bookNames: BookNames): Call<RecommendationResponse> {
        return recommendApi.getRecommendations(bookNames)
    }

}