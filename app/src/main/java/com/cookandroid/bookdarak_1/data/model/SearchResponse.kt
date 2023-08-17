package com.cookandroid.bookdarak_1.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(
    @field:Json(name = "documents")
    val documents: List<FBook>,
    @field:Json(name = "meta")
    val meta: Meta
)