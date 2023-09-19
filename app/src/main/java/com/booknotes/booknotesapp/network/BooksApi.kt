package com.booknotes.booknotesapp.network

import com.booknotes.booknotessapp.BooksFromJson
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksApi {
    @GET("volumes")
    suspend fun searchBook(
        @Query("q") title: String,
        @Query("maxResults") maxResults: Int,
    ): BooksFromJson
}