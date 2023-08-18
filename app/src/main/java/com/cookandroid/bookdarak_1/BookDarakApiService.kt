package com.cookandroid.bookdarak_1

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Path

interface BookDarakApiService {
    @POST("/signup")
    fun signup(@Body signupRequest: SignupRequest): Call<SignupResponse>

    @POST("/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("/books/recommend/age/{userId}")
    fun getBookRecommendation(@Path("userId") userId: Int): Call<RecommendationResponse>

}