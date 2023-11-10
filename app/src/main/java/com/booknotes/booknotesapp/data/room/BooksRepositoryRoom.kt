package com.booknotes.booknotesapp.data.room

import androidx.lifecycle.LiveData
import com.booknotes.booknotesapp.data.BookItemDao

interface BooksRepositoryRoom {

    suspend fun allFavouriteBooks(userId: String):LiveData<List<BookEntity>>
    suspend fun insertFavouriteBook(bookItem: BookEntity, onSuccess: () -> Unit)
    suspend fun deleteFavouriteBook(bookItem: BookEntity, onSuccess: () -> Unit)

}

class DatabaseBooksRepository(private val booksDao: BookItemDao) : BooksRepositoryRoom {

    override suspend fun allFavouriteBooks(userId: String): LiveData<List<BookEntity>> {
        return booksDao.getAllFavouriteBooks(userId)
    }

    override suspend fun insertFavouriteBook(bookItem: BookEntity, onSuccess: () -> Unit) {
        booksDao.insertFavoriteBook(bookItem)
        onSuccess()
    }

    override suspend fun deleteFavouriteBook(bookItem: BookEntity, onSuccess: () -> Unit) {
        booksDao.deleteFavoriteBook(bookItem)
        onSuccess()
    }

}
