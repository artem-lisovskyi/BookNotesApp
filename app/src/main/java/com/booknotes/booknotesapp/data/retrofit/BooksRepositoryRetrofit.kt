package com.booknotes.booknotesapp.data.retrofit

import com.booknotes.booknotesapp.network.BooksApi

interface BooksRepositoryRetrofit {
    suspend fun getBooks(
        query: String,
        maxResults: Int,
        langRestrict: String,
        orderBy: String
    ): List<Book>

    suspend fun getBookById(bookId: String): Book
}

class NetworkBooksRepository(
    private val bookApi: BooksApi
) : BooksRepositoryRetrofit {
    override suspend fun getBooks(
        query: String,
        maxResults: Int,
        langRestrict: String,
        orderBy: String
    ): List<Book> =
        bookApi.searchBook(query, maxResults, langRestrict, orderBy).items.map { items ->
            Book(
                id = items.id!!,
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
            id = bookItem.id!!,
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