package com.booknotes.booknotesapp.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_books")
data class BookEntity(
    @PrimaryKey val id: String,
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