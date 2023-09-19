package com.booknotes.booknotesapp

import android.app.Application
import com.booknotes.booknotesapp.data.AppContainer
import com.booknotes.booknotesapp.data.DefaultAppContainer

class BooksApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}