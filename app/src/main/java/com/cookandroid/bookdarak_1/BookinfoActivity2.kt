package com.cookandroid.bookdarak_1

import adapter.Bookinfo_ReviewAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cookandroid.bookdarak_1.data.api.FindBookAPI
import com.cookandroid.bookdarak_1.data.model.BookInfo_home
import com.cookandroid.bookdarak_1.data.model.SearchResponse
import com.cookandroid.bookdarak_1.databinding.ActivityBookinfo2Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookinfoActivity2 : AppCompatActivity() {
    private var bookmarked = false
    private lateinit var binding: ActivityBookinfo2Binding





    private var userId: Int = -1
    private var bookId: Int = -1
    private var isbn_of_home: String = "-1"
    val TAG: String = "BookinfoActivity2"
    lateinit var bookinforeviewRecyclerView: RecyclerView // RecyclerView adapter
    val sort = "DESC"
    private lateinit var bookinfo_reviewAdapter: Bookinfo_ReviewAdapter
    private lateinit var bookService: FindBookAPI












    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookinfo2Binding.inflate(layoutInflater)
        setContentView(binding.root)





        bookinfo_reviewAdapter = Bookinfo_ReviewAdapter(mutableListOf()) // Initialize ReviewAdapter instance



        // 다른 액티비티에서 ID를 받아옴
        val receivedIntent = intent


        userId = receivedIntent.getIntExtra("USER_ID", -1)
        isbn_of_home = receivedIntent.getStringExtra("isbn_of_home").toString()
        //bookId = intent.getIntExtra("BOOK_ID", -1)

        Log.d(TAG, "Received USER_ID, isbn_of_home: $userId,$isbn_of_home")

        val split_isbn = isbn_of_home.split(" ") // 공백을 기준으로 문자열을 분할
        val frontisbn = split_isbn[0]






            initBookService()

            bookService.FindBook(frontisbn).enqueue(object : Callback<SearchResponse> {override fun onResponse(
                call: Call<SearchResponse>, response: Response<SearchResponse>
            ) {
                if (response.isSuccessful.not()) {
                    return
                }


                val results_of_isbn_of_home = response.body()?.documents

                Log.d("BookinfoActivity2", "results_of_isbn_of_home: $results_of_isbn_of_home")
                val firstBookInfo = results_of_isbn_of_home?.firstOrNull()

                // firstBookInfo가 null이 아닌 경우, 해당 정보를 화면에 표시합니다.
                val bookinfo_home = BookInfo_home (
                firstBookInfo?.title,
                firstBookInfo?.contents,
                firstBookInfo?.publisher,
                firstBookInfo?.datetime,
                    firstBookInfo?.price ?: 0,
                    firstBookInfo?.isbn,
                    firstBookInfo?.authors,
                    firstBookInfo?.thumbnail

                )

                binding.bookname2.text = bookinfo_home.title.orEmpty()
                binding.bookcontent2.text = bookinfo_home.contents.orEmpty()
                binding.publisher2.text = bookinfo_home.publisher.orEmpty()
                binding.publishdate2.text = bookinfo_home.datetime.orEmpty()
                binding.price2.text = bookinfo_home.price.toString()
                binding.textIsbn2.text = frontisbn
                binding.writer2.text = bookinfo_home.authors.toString()



                Glide.with(binding.imageView2.context)
                .load(bookinfo_home.thumbnail.orEmpty())
                .into(binding.imageView2)

                    val booktitle = bookinfo_home.title.toString()
                    val authorList = bookinfo_home.authors ?: emptyList()
                    val fullisbn = bookinfo_home.isbn.toString().split(" ")
                    val firstisbn = fullisbn[0]
                    Log.d(TAG, "rbookidrequest0: $fullisbn,$firstisbn")
                    val content = bookinfo_home.contents.toString()
                    val image = bookinfo_home.thumbnail.toString()


                    Log.d(TAG, "rbookidrequest: $booktitle,$firstisbn,$image,$authorList")
                    val BookIdrequest = BookIdRequest(booktitle, authorList, firstisbn, image) // gender 추가
                    Log.d(TAG, "rbookidrequest_2: $BookIdrequest")

                    ApiClient.service.bookId(BookIdrequest).enqueue(object: Callback<BookIdResponse> {
                        override fun onResponse(call: Call<BookIdResponse>, response: Response<BookIdResponse>) {
                            if (response.isSuccessful  && response.body()?.isSuccess == true) {
                                Log.d(TAG, "rbookidrequest_3: $authorList")
                                //val bookId = response.body()?.result?.bookId ?: -1
                                val bookId = response.body()?.result?.id ?: -1  // <-- 'bookId'를 'id'로 수정
                                Log.d(TAG, "wwbookID: $bookId")



                                ApiClient.service.getReviewId(userId, bookId).enqueue(object: Callback<ReviewIdResponse> {
                                    override fun onResponse(call: Call<ReviewIdResponse>, response: Response<ReviewIdResponse>) {
                                        if (response.isSuccessful) {
                                            val reviewId = response.body()?.result?.reviewId

                                            if(reviewId == -1) {
                                                val writeButton2: Button = findViewById(R.id.writebutton2)

                                                writeButton2.text = "기록하기"
                                                writeButton2.setOnClickListener {

                                                    val intent = Intent(this@BookinfoActivity2, writingreview2::class.java)
                                                    intent.putExtra("bookinfo_home", bookinfo_home)
                                                    intent.putExtra("USER_ID", userId) // Passing userId to writingreview activity
                                                    intent.putExtra("BOOK_ID", bookId)
                                                    intent.putExtra("FRONT_ISBN", frontisbn)
                                                    Log.d(TAG, "BookinfoActivity2_user and bookID: $userId, $bookId, $bookinfo_home,$firstisbn")
                                                    startActivity(intent)




                                                }

                                            }
                                            else{

                                                val writeButton2: Button = findViewById(R.id.writebutton2)


                                                writeButton2.text = "수정하기"
                                                writeButton2.setOnClickListener {

                                                    val intent = Intent(this@BookinfoActivity2, editreview2::class.java)
                                                    intent.putExtra("bookinfo_home", bookinfo_home)
                                                    intent.putExtra("USER_ID", userId) // Passing userId to writingreview activity
                                                    intent.putExtra("BOOK_ID", bookId)
                                                    intent.putExtra("REVIEW_ID", reviewId)
                                                    intent.putExtra("FRONT_ISBN", frontisbn)
                                                    Log.d(TAG, "BookinfoActivity2_user and bookID and reviewId: $userId, $bookId ,$reviewId,$bookinfo_home,$firstisbn")
                                                    startActivity(intent)




                                                }
                                            }


                                        } else {
                                            Toast.makeText(this@BookinfoActivity2, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                    override fun onFailure(call: Call<ReviewIdResponse>, t: Throwable) {
                                        Toast.makeText(this@BookinfoActivity2, t.localizedMessage, Toast.LENGTH_SHORT).show()
                                    }
                                })




                                ApiClient.service.getBookReviews(bookId,sort).enqueue(object :
                                    Callback<ReviewListResponse> {
                                    override fun onResponse(call: Call<ReviewListResponse>, response: Response<ReviewListResponse>) {
                                        if (response.isSuccessful) {
                                            val results = response.body()?.result?.items
                                            Log.d("BookinfoActivity2", "Review_results: $results")

                                            // Set data to adapter when data is received normally
                                            results?.let {
                                                bookinfo_reviewAdapter.submitData(it)
                                            }

                                        } else {
                                            Log.e("BookinfoActivity2", "Server returned error: ${response.code()} - ${response.message()}")
                                            response.body()?.let {
                                                Log.e("BookinfoActivity2", "Error code: ${it.code} - ${it.message}")
                                            }
                                        }
                                    }

                                    override fun onFailure(call: Call<ReviewListResponse>, t: Throwable) {
                                        Log.e("BookinfoActivity2", "Error fetching data: ${t.message}")
                                    }
                                })

                                val bookmarkButton2 = findViewById<ImageButton>(R.id.bookmarkButton2)

                                var isBookmarked = false
                                bookmarkButton2.setOnClickListener {
                                    isBookmarked = !isBookmarked
                                    if(isBookmarked){
                                        bookmarkButton2.setImageResource(R.drawable.bookmarks_icon_colored)
                                        ApiClient.service.addBookmark(userId, bookId).enqueue(object :
                                            Callback<BookmarkResponse> {
                                            override fun onResponse(call: Call<BookmarkResponse>, response: Response<BookmarkResponse>) {
                                                if (response.isSuccessful) {
                                                    if (response.body()?.isSuccess == true) {
                                                        // 북마크 추가 성공
                                                        Toast.makeText(this@BookinfoActivity2,"북마크 목록에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                                                        val message = response.body()?.message

                                                    } else {
                                                        // 북마크 추가 실패
                                                        Toast.makeText(this@BookinfoActivity2,"북마크 목록 추가가 실패하였습니다.", Toast.LENGTH_SHORT).show()
                                                        val errorMessage = response.body()?.message

                                                    }
                                                } else {
                                                    // 서버 응답이 실패한 경우 처리
                                                }
                                            }

                                            override fun onFailure(call: Call<BookmarkResponse>, t: Throwable) {
                                                // 네트워크 오류 등 처리
                                            }
                                        })


                                    } else {


                                        bookmarkButton2.setImageResource(R.drawable.bookmarks_icon)
                                        ApiClient.service.deleteBookmark(userId, bookId).enqueue(object :
                                            Callback<DeleteBookmarkResponse> {
                                            override fun onResponse(call: Call<DeleteBookmarkResponse>, response: Response<DeleteBookmarkResponse>) {
                                                if (response.isSuccessful) {
                                                    if (response.body()?.isSuccess == true) {
                                                        // 북마크 삭제 성공
                                                        Toast.makeText(this@BookinfoActivity2,"북마크를 삭제하였습니다.", Toast.LENGTH_SHORT).show()
                                                        val message = response.body()?.message
                                                        // message 등을 활용하여 사용자에게 알림 또는 작업 수행
                                                    } else {
                                                        // 북마크 삭제 실패
                                                        Toast.makeText(this@BookinfoActivity2,"북마크 삭제가 실패하였습니다.", Toast.LENGTH_SHORT).show()
                                                        val errorMessage = response.body()?.message
                                                        // errorMessage 등을 활용하여 사용자에게 알림 또는 작업 수행
                                                    }
                                                } else {
                                                    // 서버 응답이 실패한 경우 처리
                                                }
                                            }

                                            override fun onFailure(call: Call<DeleteBookmarkResponse>, t: Throwable) {
                                                // 네트워크 오류 등 처리
                                            }
                                        })
                                    }
                                }






                            } else {
                                Toast.makeText(this@BookinfoActivity2, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<BookIdResponse>, t: Throwable) {
                            Toast.makeText(this@BookinfoActivity2, t.localizedMessage, Toast.LENGTH_SHORT).show()

                        }
                    })


                }

                // 실패.
                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    Log.e(TAG, t.toString())
                }

            })







        val backButton2 = findViewById<ImageButton>(R.id.backButton2)
        val bookcontent2 = findViewById<TextView>(R.id.bookcontent2)
        val expandButton2 = findViewById<Button>(R.id.expandButton2)


        // Back button click event
        backButton2.setOnClickListener {
            finish()
        }

        // Bookmark button click event


        // Expand/Collapse button click event
        expandButton2.setOnClickListener {
            if (bookcontent2.maxLines == 5) {
                bookcontent2.maxLines = Integer.MAX_VALUE  // 전체 텍스트 표시
                expandButton2.text = "접기"  // 버튼 텍스트 변경
            } else {
                bookcontent2.maxLines = 5  // 텍스트를 5줄까지만 표시
                expandButton2.text = "자세히 보기"  // 버튼 텍스트 변경
            }

        }



        }




    private fun initBookService() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/") // 인터파크 베이스 주소;
            .addConverterFactory(GsonConverterFactory.create()) // Gson 변환기 사용;
            .build()

        bookService = retrofit.create(FindBookAPI::class.java)
    }

}









