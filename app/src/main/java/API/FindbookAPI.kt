package API


import model.FindBookDTO
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call


interface FindbookAPI {

        @GET("https://dapi.kakao.com/v3/search/book?output=json")
        fun Bookname(
            @Query("key") apiKey:String,
            @Query("query") keyword:String

        ): Call<FindBookDTO>
    }
