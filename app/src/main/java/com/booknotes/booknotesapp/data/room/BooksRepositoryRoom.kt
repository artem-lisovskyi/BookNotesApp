package com.booknotes.booknotesapp.data.room

import androidx.lifecycle.LiveData
import com.booknotes.booknotesapp.data.BookItemDao
import com.booknotes.booknotesapp.data.retrofit.Book

interface BooksRepositoryRoom {

    val allFavouriteBooks: LiveData<List<Book>>
    suspend fun insertFavouriteBook(bookItem: Book, onSuccess: () -> Unit)
    suspend fun deleteFavouriteBook(bookItem: Book, onSuccess: () -> Unit)

}

class DatabaseBooksRepository(private val booksDao: BookItemDao): BooksRepositoryRoom{
    override val allFavouriteBooks: LiveData<List<Book>>
        get() = booksDao.getAllFavouriteBooks()

    override suspend fun insertFavouriteBook(bookItem: Book, onSuccess: () -> Unit) {
        booksDao.insertFavoriteBook(bookItem)
        onSuccess()
    }

    override suspend fun deleteFavouriteBook(bookItem: Book, onSuccess: () -> Unit) {
        booksDao.deleteFavoriteBook(bookItem)
        onSuccess()
    }

}
