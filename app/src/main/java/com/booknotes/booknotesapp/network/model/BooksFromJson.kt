package com.booknotes.booknotessapp

import com.google.gson.annotations.SerializedName


data class BooksFromJson(

    @SerializedName("kind") var kind: String? = null,
    @SerializedName("totalItems") var totalItems: Int? = null,
    @SerializedName("items") var items: ArrayList<Items> = arrayListOf()

)