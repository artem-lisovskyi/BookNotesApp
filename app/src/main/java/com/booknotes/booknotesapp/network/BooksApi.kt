package com.booknotes.booknotesapp.network

import com.booknotes.booknotessapp.BooksFromJson
import com.booknotes.booknotessapp.Items
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BooksApi {
    @GET("volumes")
    suspend fun searchBook(
        @Query("q") title: String,
        @Query("maxResults") maxResults: Int,
    ): BooksFromJson

    @GET("volumes/{volumeId}")
    suspend fun searchBookById(
        @Path("volumeId") volumeId: String
    ): Items
}