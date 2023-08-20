package com.cookandroid.bookdarak_1.data.model



import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FBook(

    val authors: List<String>,
    val contents: String,
    val datetime: String,

    val isbn: String,
    val price: Int,
    val publisher: String,

    val salePrice: Int,
    val status: String,
    val thumbnail: String,
    val title: String,
    val translators: List<String>,
    val url: String
):Parcelable