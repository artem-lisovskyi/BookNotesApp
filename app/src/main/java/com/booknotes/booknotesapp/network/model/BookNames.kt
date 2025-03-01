package com.booknotes.booknotesapp.network.model

import com.google.gson.annotations.SerializedName

data class BookNames(
    @SerializedName("book_names") var book_names: List<String>? = null
)
