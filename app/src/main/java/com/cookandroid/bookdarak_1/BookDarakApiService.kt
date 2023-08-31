package com.cookandroid.bookdarak_1

import retrofit2.Call
import retrofit2.http.*

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

    @GET("/reviews/{reviewId}")
    fun getReviewDetail(@Path("reviewId") reviewId: Int): Call<ReviewDetailResponse>

    @GET("/reviews/{userId}/{bookId}")
    fun getReviewId(
        @Path("userId") userId: Int,
        @Path("bookId") bookId: Int
    ): Call<ReviewIdResponse>

    @PATCH("/reviews/{reviewId}")
    fun updateReview(
        @Path("reviewId") reviewId: Int,
        @Body updateReviewRequest: UpdateReviewRequest
    ): Call<UpdateReviewResponse>

    @DELETE("/reviews/{userId}/{bookId}")
    fun deleteReview(
        @Path("userId") userId: Int,
        @Path("bookId") bookId: Int
    ): Call<DeleteReviewResponse>

    @POST("/bookmarks/{userId}/{bookId}")
    fun addBookmark(@Path("userId") userId: Int, @Path("bookId") bookId: Int): Call<BookmarkResponse>

    @GET("/bookmarks/{userId}/{bookId}")
    fun checkBookmark(@Path("userId") userId: Int, @Path("bookId") bookId: Int): Call<BookmarkCheckResponse>

    @DELETE("/bookmarks/{userId}/{bookId}")
    fun deleteBookmark(
        @Path("userId") userId: Int,
        @Path("bookId") bookId: Int
    ): Call<DeleteBookmarkResponse>

    @GET("/calendar/{userId}")
    fun getCalendarData(
        @Path("userId") userId: Int,
        @Query("startD") startDate: String,
        @Query("endD") endDate: String
    ): Call<CalendarResponse>

    @GET("/books/{bookId}")
    fun getBookDetail(@Path("bookId") bookId: Int): Call<BookResponse>

    @POST("/books")
    fun bookId(
        @Body bookIdRequest: BookIdRequest
    ): Call<BookIdResponse>




}