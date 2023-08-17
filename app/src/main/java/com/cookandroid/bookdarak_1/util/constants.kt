package com.cookandroid.bookdarak_1.util

import com.cookandroid.bookdarak_1.BuildConfig

object constants {

    const val BASE_URL = "https://dapi.kakao.com/"
    const val API_KEY = BuildConfig.bookApiKey
    const val SEARCH_BOOKS_TIME_DELAY = 100L
    const val DATASTORE_NAME = "preferences_datastore"
    const val PAGING_SIZE = 15
}