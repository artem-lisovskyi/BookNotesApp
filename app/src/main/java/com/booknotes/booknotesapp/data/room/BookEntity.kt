package com.booknotes.booknotesapp.data.room

import androidx.room.Entity

@Entity(tableName = "favourite_books", primaryKeys = ["id", "userId"])
data class BookEntity(
    val id: String,
    val userId: String,
    val title: String?,
    val authors: List<String>?,
    val publishedDate: String?,
    val description: String?,
    val pageCount: Int?,
    val categories: List<String>?,
    val imageLink: String?,
    val previewLink: String?
)