package com.booknotes.booknotesapp.data.retrofit

import com.booknotes.booknotesapp.network.BooksApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val booksRepositoryRetrofit: BooksRepositoryRetrofit
}

class DefaultAppContainer : AppContainer {
    private val BASE_URL = "https://www.googleapis.com/books/v1/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: BooksApi by lazy {
        retrofit.create(BooksApi::class.java)
    }

    override val booksRepositoryRetrofit: BooksRepositoryRetrofit by lazy {
        NetworkBooksRepository(retrofitService)
    }
}