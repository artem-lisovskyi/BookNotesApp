package com.booknotes.booknotesapp.data

import com.booknotes.booknotesapp.network.BooksApi

interface BooksRepository{
    suspend fun getBooks(query: String, maxResults: Int): List<Book>
    suspend fun getBookById(bookId: String): Book
}

class NetworkBooksRepository(
    private val bookApi: BooksApi
) : BooksRepository {
    override suspend fun getBooks(
        query: String,
        maxResults: Int
    ): List<Book> = bookApi.searchBook(query, maxResults).items.map { items ->
        Book(
            id = items.id,
            title = items.volumeInfo?.title,
            authors = items.volumeInfo?.authors,
            publishedDate = items.volumeInfo?.publishedDate,
            description = items.volumeInfo?.description,
            pageCount = items.volumeInfo?.pageCount,
            categories = items.volumeInfo?.categories,
            imageLink = items.volumeInfo?.imageLinks?.smallThumbnail,
            previewLink = items.volumeInfo?.previewLink
        )
    }

    override suspend fun getBookById(bookId: String): Book {
        val bookItem = bookApi.searchBookById(volumeId = bookId.drop(1).dropLast(1))
        return Book(
            id = bookItem.id,
            title = bookItem.volumeInfo?.title,
            authors = bookItem.volumeInfo?.authors,
            publishedDate = bookItem.volumeInfo?.publishedDate,
            description = bookItem.volumeInfo?.description,
            pageCount = bookItem.volumeInfo?.pageCount,
            categories = bookItem.volumeInfo?.categories,
            imageLink = bookItem.volumeInfo?.imageLinks?.smallThumbnail,
            previewLink = bookItem.volumeInfo?.previewLink
        )
    }
}