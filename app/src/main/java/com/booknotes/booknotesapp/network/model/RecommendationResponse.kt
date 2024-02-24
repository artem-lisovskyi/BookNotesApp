package com.booknotes.booknotesapp.network.model

import com.google.gson.annotations.SerializedName


data class RecommendationResponse(
    @SerializedName("recommendations") var recommendations: List<String>? = null
)