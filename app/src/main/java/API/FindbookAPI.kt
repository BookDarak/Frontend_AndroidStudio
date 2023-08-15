package API


import model.FindBookListDTO
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.http.Headers


interface FindbookAPI {
        @Headers("Authorization: KakaoAK $API_KEY")
        @GET("v3/search/book")
        fun Bookname(
            //@Query("key") apiKey:String,>요청파라미터에 key는 없음
            @Query("query") keyword:String

        ): Call<FindBookListDTO>
    }
