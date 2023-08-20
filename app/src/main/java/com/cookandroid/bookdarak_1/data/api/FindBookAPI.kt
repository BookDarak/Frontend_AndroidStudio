package com.cookandroid.bookdarak_1.data.api

import com.cookandroid.bookdarak_1.data.model.SearchResponse
import com.cookandroid.bookdarak_1.util.constants.API_KEY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface FindBookAPI {

    @Headers("Authorization: KakaoAK $API_KEY")
    @GET("v3/search/book")
    fun FindBook(

        @Query("query") keyword:String



    ): Call<SearchResponse>
}
