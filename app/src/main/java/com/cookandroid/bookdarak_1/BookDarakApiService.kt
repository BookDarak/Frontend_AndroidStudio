package com.cookandroid.bookdarak_1

import retrofit2.Call
import retrofit2.http.*

interface BookDarakApiService {
    @POST("/signup") //회원가입
    fun signup(@Body signupRequest: SignupRequest): Call<SignupResponse>

    @POST("/login") //로그인
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("/books/recommend/age/{userId}")   //연령별 도서 추천
    fun getBookRecommendation(@Path("userId") userId: Int): Call<RecommendationResponse>

    @GET("/users/info/{userId}")   //유저 정보 조회
    fun getUserInfo(@Path("userId") userId: Int): Call<UserInfoResponse>

    @GET("/books/recommend/gender/{userId}")    //성별 도서 추천
    fun getGenderBasedRecommendation(@Path("userId") userId: Int): Call<RecommendationResponse>

    @POST("/gpt/quote")    //명언조회
    fun getQuote(): Call<QuoteResponse>

    @GET("/users/day/{userId}")   //유저 일자 조회
    fun getUserDay(@Path("userId") userId: Int): Call<UserDayResponse>

    @POST("/reviews/{userId}/{bookId}")    //서평 작성
    fun writeReview(
        @Path("userId") userId: Int,
        @Path("bookId") bookId: Int,
        @Body reviewRequest: ReviewRequest
    ): Call<ReviewResponse>

    @GET("/reviews/{reviewId}")    //서평 조회
    fun getReviewDetail(@Path("reviewId") reviewId: Int): Call<ReviewDetailResponse>

    @GET("/reviews/{userId}/{bookId}")    //리뷰 아이디 조회
    fun getReviewId(
        @Path("userId") userId: Int,
        @Path("bookId") bookId: Int
    ): Call<ReviewIdResponse>

    @PATCH("/reviews/{reviewId}")    //서평 수정
    fun updateReview(
        @Path("reviewId") reviewId: Int,
        @Body updateReviewRequest: UpdateReviewRequest
    ): Call<UpdateReviewResponse>

    @DELETE("/reviews/{userId}/{bookId}")  //서평 삭제(수정 필요)
    fun deleteReview(
        @Path("userId") userId: Int,
        @Path("bookId") bookId: Int
    ): Call<DeleteReviewResponse>

    @POST("/bookmarks/{userId}/{bookId}")    //북마크 추가
    fun addBookmark(@Path("userId") userId: Int, @Path("bookId") bookId: Int): Call<BookmarkResponse>

    @GET("/bookmarks/{userId}/{bookId}")   //북마크 여부 조회
    fun checkBookmark(@Path("userId") userId: Int, @Path("bookId") bookId: Int): Call<BookmarkCheckResponse>

    @DELETE("/bookmarks/{userId}/{bookId}")   //북마크 삭제
    fun deleteBookmark(
        @Path("userId") userId: Int,
        @Path("bookId") bookId: Int
    ): Call<DeleteBookmarkResponse>

    @GET("/calendar/{userId}")   //달력 조회
    fun getCalendarData(
        @Path("userId") userId: Int,
        @Query("startD") startDate: String,
        @Query("endD") endDate: String
    ): Call<CalendarResponse>

    @GET("/books/{bookId}")   //책 조회
    fun getBookDetail(@Path("bookId") bookId: Int): Call<BookResponse>

    @POST("/books")   //책 검색 및 저장
    fun bookId(
        @Body bookIdRequest: BookIdRequest
    ): Call<BookIdResponse>



    @GET("/reviews/shorts/users/{userId}")   //유저 요약 서평 모두 조회
    fun getUserReviews(
        @Path("userId") userId: Int,
        @Query("isOwner") isOwner: String,
        @Query("sort") sort: String? = null
    ): Call<UserReviewResponse>

    @GET("/reviews/shorts/books/{bookId}")   //책 공개 서평 조회
    fun getBookReviews(
        @Path("bookId") bookId: Int,
        @Query("sort") sort: String? = "createdAt,DESC"
    ): Call<ReviewListResponse>

    @GET("/reviews/shorts")   //공개 요약 서평 조회
    fun getPublicSummaryReviews(
        @Query("sort") sort: String? = "createdAt,DESC"
    ): Call<ReviewSummaryResponse>

    @GET("/reviews/shorts/recommend/{reviewId}")   //요약서평추천수조회
    fun getRecommendCountByReviewId(
        @Path("reviewId") reviewId: Int
    ): Call<RecommendCountResponse>

    @GET("/reviews/shorts/recommend/{userId}/{reviewId}")   //요약서평추천여부조회
    fun checkRecommendStatus(
        @Path("userId") userId: Int,
        @Path("reviewId") reviewId: Int
    ): Call<RecommendStatusResponse>

    @DELETE("/reviews/shorts/recommend/{userId}/{reviewId}")   //요약서평추천삭제
    fun deleteRecommendation(
        @Path("userId") userId: Int,
        @Path("reviewId") reviewId: Int
    ): Call<RecommendDeleteResponse>

    @POST("/reviews/shorts/recommend/{userId}/{reviewId}")
    fun addRecommendation(
        @Path("userId") userId: Int,
        @Path("reviewId") reviewId: Int
    ): Call<RecommendResponse>
}