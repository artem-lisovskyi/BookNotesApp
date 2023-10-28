package com.booknotes.booknotesapp.data.retrofit

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_books")
data class Book(
    @PrimaryKey val id: String,
    val title: String?,
    val authors: List<String>?,
    val publishedDate: String?,
    val description: String?,
    val pageCount: Int?,
    val categories: List<String>?,
    val imageLink: String?,
    val previewLink: String?
)