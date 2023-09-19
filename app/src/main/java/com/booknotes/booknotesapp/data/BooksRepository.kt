package com.booknotes.booknotesapp.data

import com.booknotes.booknotesapp.network.BooksApi

interface BooksRepository{
    suspend fun getBooks(query: String, maxResults: Int): List<Book>
}

class NetworkBooksRepository(
    private val bookApi: BooksApi
): BooksRepository{
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
}