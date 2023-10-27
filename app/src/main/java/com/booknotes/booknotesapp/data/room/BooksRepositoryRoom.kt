package com.booknotes.booknotesapp.data.room

import androidx.lifecycle.LiveData
import com.booknotes.booknotesapp.data.BookItemDao
import com.booknotes.booknotesapp.data.Favorites

interface BooksRepositoryRoom {

    val allFavouriteBooks: LiveData<List<Favorites>>
    suspend fun insertFavouriteBook(bookItem: Favorites, onSuccess: () -> Unit)
    suspend fun deleteFavouriteBook(bookItem: Favorites, onSuccess: () -> Unit)

}

class DatabaseBooksRepository(private val booksDao: BookItemDao): BooksRepositoryRoom{
    override val allFavouriteBooks: LiveData<List<Favorites>>
        get() = booksDao.getAllFavouriteBooks()

    override suspend fun insertFavouriteBook(bookItem: Favorites, onSuccess: () -> Unit) {
        booksDao.insertFavoriteBook(bookItem)
        onSuccess()
    }

    override suspend fun deleteFavouriteBook(bookItem: Favorites, onSuccess: () -> Unit) {
        booksDao.deleteFavoriteBook(bookItem)
        onSuccess()
    }

}
