package com.booknotes.booknotesapp.data.retrofit

data class Book(
    val id: String?,
    val title: String?,
    val authors: List<String>?,
    val publishedDate: String?,
    val description: String?,
    val pageCount: Int?,
    val categories: List<String>?,
    val imageLink: String?,
    val previewLink: String?
)