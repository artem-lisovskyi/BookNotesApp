package com.booknotes.booknotesapp

import android.app.Application
import com.booknotes.booknotesapp.data.AppDatabase
import com.booknotes.booknotesapp.data.retrofit.AppContainer
import com.booknotes.booknotesapp.data.retrofit.DefaultAppContainer
import com.booknotes.booknotesapp.data.retrofit.RecommendContainer
import com.booknotes.booknotesapp.data.retrofit.RecommendationAppContainer
import com.booknotes.booknotesapp.data.room.DatabaseBooksRepository

class BooksApplication : Application() {
    lateinit var container: AppContainer
    lateinit var  recommendContainer: RecommendContainer
    private lateinit var dbBookRepository: DatabaseBooksRepository
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
        recommendContainer = RecommendationAppContainer()
        val daoBook = AppDatabase.getInstance(applicationContext).bookItemDao()
        dbBookRepository = DatabaseBooksRepository(daoBook)
    }

    fun getDatabase(): DatabaseBooksRepository {
        return dbBookRepository
    }
}