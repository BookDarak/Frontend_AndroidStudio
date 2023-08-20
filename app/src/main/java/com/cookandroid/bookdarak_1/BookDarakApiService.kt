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

    @GET("/users/info/{userId}")
    fun getUserInfo(@Path("userId") userId: Int): Call<UserInfoResponse>

    @GET("/books/recommend/gender/{userId}")
    fun getGenderBasedRecommendation(@Path("userId") userId: Int): Call<RecommendationResponse>

    @POST("/gpt/quote")
    fun getQuote(): Call<QuoteResponse>

    @GET("/users/day/{userId}")
    fun getUserDay(@Path("userId") userId: Int): Call<UserDayResponse>

    @POST("/reviews/{userId}/{bookId}")
    fun writeReview(
        @Path("userId") userId: Int,
        @Path("bookId") bookId: Int,
        @Body reviewRequest: ReviewRequest
    ): Call<ReviewResponse>

}