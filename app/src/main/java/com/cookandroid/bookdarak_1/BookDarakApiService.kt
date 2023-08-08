package com.cookandroid.bookdarak_1

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface BookDarakApiService {
    @POST("/signup")
    fun signup(@Body signupRequest: SignupRequest): Call<SignupResponse>

    @POST("/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
}
