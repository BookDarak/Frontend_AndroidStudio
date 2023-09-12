package com.cookandroid.bookdarak_1.data.model

import java.io.Serializable

data class BookInfo_home(
    val title: String?,
    val contents: String?,
    val publisher: String?,
    val datetime: String?,
    val price: Int,
    val isbn: String?,
    val authors: List<String>?,
    val thumbnail: String?,
) : Serializable
