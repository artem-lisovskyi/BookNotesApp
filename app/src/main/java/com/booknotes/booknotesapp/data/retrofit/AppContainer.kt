package com.booknotes.booknotesapp.data.retrofit

import com.booknotes.booknotesapp.network.BooksApi
import com.booknotes.booknotesapp.network.RecommendationApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

interface AppContainer {
    val booksRepositoryRetrofit: BooksRepositoryRetrofit
}

interface RecommendContainer {
    val recommendationRetrofit: RecommendationRetrofit
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

class RecommendationAppContainer : RecommendContainer {
    private val BASE_URL = "https://recommend-server-las.azurewebsites.net/"

    private val okHttpClient = OkHttpClient.Builder()
        .readTimeout(200, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    private val retrofitService: RecommendationApi by lazy {
        retrofit.create(RecommendationApi::class.java)
    }

    override val recommendationRetrofit: NetworkRecommendationRepository by lazy {
        NetworkRecommendationRepository(retrofitService)
    }
}