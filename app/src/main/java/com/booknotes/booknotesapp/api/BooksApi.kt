package com.booknotes.booknotesapp.api

import com.booknotes.booknotesapp.model.Books
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksApi {
    @GET("volumes")
    suspend fun getBooks(
        @Query("q") title: String,
        @Query("maxResults") maxResults: Int = 40,
    ): Response<Books>
}