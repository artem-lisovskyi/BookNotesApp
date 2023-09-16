package com.booknotes.booknotesapp.model

import com.google.gson.annotations.SerializedName

data class Books(
    @SerializedName("items")
    var items: List<Book>? = null
)

data class Book(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("volumeInfo")
    var volumeInfo: VolumeInfo? = null
)

data class VolumeInfo(
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("authors")
    var authors: List<String>? = null,
    @SerializedName("publishedDate")
    var publishedDate: String? = null,
    @SerializedName("description")
    var description: String? = null,
    @SerializedName("pageCount")
    var pageCount: Int? = null,
    @SerializedName("categories")
    var categories: List<String>? = null,
    @SerializedName("imageLinks")
    var imageLinks: Images? = null
)

data class Images(
    @SerializedName("smallThumbnail")
    var smallThumbnail: String? = null
)