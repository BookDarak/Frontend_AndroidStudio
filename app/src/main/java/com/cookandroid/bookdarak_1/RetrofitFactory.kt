// RetrofitFactory.kt
package com.cookandroid.bookdarak_1

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {
    private const val BASE_URL = "https://YOUR_API_ENDPOINT/"  // 여기에 실제 API endpoint를 입력해주세요.

    fun create(): BookDarakApiService {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()

        return retrofit.create(BookDarakApiService::class.java)
    }
}
