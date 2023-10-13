package com.cookandroid.bookdarak_1

import adapter.Bookinfo_ReviewAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cookandroid.bookdarak_1.data.api.FindBookAPI
import com.cookandroid.bookdarak_1.data.model.FBook
import com.cookandroid.bookdarak_1.databinding.ActivityBookinfoBinding
import com.cookandroid.bookdarak_1.ui.adapter.BookSearchViewHolder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookinfoActivity : AppCompatActivity() {
    private var bookmarked = false
    private lateinit var binding: ActivityBookinfoBinding





    private var model: FBook? = null
    private var userId: Int = -1
    private var bookId: Int = -1
    private var reviewId: Int = -1
    val TAG: String = "BookinfoActivity"
    lateinit var bookinfo_reviewrecyclerview: RecyclerView // RecyclerView adapter
    val sort = "DESC"
    private lateinit var bookinfo_reviewAdapter: Bookinfo_ReviewAdapter
    private lateinit var bookRecyclerViewAdapter: BookSearchViewHolder
    private lateinit var bookService: FindBookAPI













    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isReviewEdited = intent.getBooleanExtra("REVIEW_EDITED", false)





        bookinfo_reviewAdapter = Bookinfo_ReviewAdapter(mutableListOf())
        bookinfo_reviewrecyclerview = findViewById(R.id.bookinfo_reviewrecyclerview)
        bookinfo_reviewrecyclerview.adapter = bookinfo_reviewAdapter
        val layoutManager = LinearLayoutManager(this)
        bookinfo_reviewrecyclerview.layoutManager = layoutManager





        val receivedIntent = intent


        userId = receivedIntent.getIntExtra("USER_ID", -1)


        Log.d(TAG, "Received USER_ID: $userId")

        model = intent.getParcelableExtra("bookModel")
        reviewId = intent.getIntExtra("REVIEW_ID",-1)

        val writerText = model?.authors.toString()
        val booktitle = model?.title.toString()
        val authorList = model?.authors ?: emptyList()

        val fullisbn = model?.isbn.toString()
        val split_isbn = fullisbn.split(" ") // 공백을 기준으로 문자열을 분할
        val frontisbn = split_isbn[0]


        val image = model?.thumbnail.toString()
        val writeButton: Button = findViewById(R.id.writebutton)

        Log.d(TAG, "rbookidrequest: $booktitle,$frontisbn,$image,$authorList")
        val BookIdrequest = BookIdRequest(booktitle, authorList, frontisbn, image) // gender 추가
        Log.d(TAG, "rbookidrequest_2: $BookIdrequest")




        val moreReview = findViewById<TextView>(R.id.text_more_review)


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

                                if(isReviewEdited||reviewId != -1) {

                                    writeButton.text = "수정하기"
                                    writeButton.setOnClickListener {

                                        val intent = Intent(this@BookinfoActivity, editreview::class.java)
                                        intent.putExtra("bookModel", model)
                                        intent.putExtra("USER_ID", userId) // Passing userId to writingreview activity
                                        intent.putExtra("BOOK_ID", bookId)
                                        intent.putExtra("REVIEW_ID", reviewId)
                                        Log.d(TAG, "BookinfoActivity_user and bookID and reviewId: $userId, $bookId ,$reviewId")
                                        startActivity(intent)






                                    }

                                    }
                                else{

                                    writeButton.text = "기록하기"
                                    writeButton.setOnClickListener {

                                        val intent = Intent(this@BookinfoActivity, writingreview::class.java)
                                        intent.putExtra("bookModel", model)
                                        intent.putExtra("USER_ID", userId) // Passing userId to writingreview activity
                                        intent.putExtra("BOOK_ID", bookId)
                                        Log.d(TAG, "BookinfoActivity_user and bookID: $userId, $bookId")
                                        startActivity(intent)



                                    }
                                }


                            } else {
                                when(response.body()?.code) {
                                    2024 -> dialog("존재하지 않는 유저 id입니다.")
                                    2033 -> dialog("존재하지 않는 책 id입니다.")
                                    //else -> dialog("fail")
                                }
                            }
                        }

                        override fun onFailure(call: Call<ReviewIdResponse>, t: Throwable) {
                            Toast.makeText(this@BookinfoActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                    })


                    moreReview.setOnClickListener {
                        val intent = Intent(this@BookinfoActivity, certain_bookreview::class.java)

                        intent.putExtra("USER_ID", userId) // Passing userId to writingreview activity
                        intent.putExtra("BOOK_ID", bookId)
                        Log.d(TAG, "BookinfoActivity_user and bookID_2: $userId, $bookId")
                        startActivity(intent)

                    }




                    ApiClient.service.getBookReviews(bookId,sort).enqueue(object : Callback<ReviewListResponse> {
                        override fun onResponse(call: Call<ReviewListResponse>, response: Response<ReviewListResponse>) {
                            if (response.isSuccessful) {
                                val results = response.body()?.result?.items
                                Log.d("BookinfoActivity", "Review_results: $results")

                                // Set data to adapter when data is received normally
                                results?.let {
                                    bookinfo_reviewAdapter.submitData(it)
                                }

                            } else {
                                Log.e("BookinfoActivity", "Server returned error: ${response.code()} - ${response.message()}")
                                response.body()?.let {
                                    Log.e("BookinfoActivity", "Error code: ${it.code} - ${it.message}")
                                }
                            }
                        }

                        override fun onFailure(call: Call<ReviewListResponse>, t: Throwable) {
                            Log.e("BookinfoActivity", "Error fetching data: ${t.message}")
                        }
                    })

                    val bookmarkButton = findViewById<ImageButton>(R.id.bookmarkButton)

                    var isBookmarked = false
                    bookmarkButton.setOnClickListener {
                        isBookmarked = !isBookmarked
                        if(isBookmarked){
                            bookmarkButton.setImageResource(R.drawable.bookmarks_icon_colored)
                            ApiClient.service.addBookmark(userId, bookId).enqueue(object : Callback<BookmarkResponse> {
                                override fun onResponse(call: Call<BookmarkResponse>, response: Response<BookmarkResponse>) {
                                    if (response.isSuccessful) {
                                        if (response.body()?.isSuccess == true) {
                                            // 북마크 추가 성공
                                            Toast.makeText(this@BookinfoActivity,"북마크 목록에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                                            val message = response.body()?.message
                                            // message 등을 활용하여 사용자에게 알림 또는 작업 수행
                                        } else {
                                            // 북마크 추가 실패
                                            Toast.makeText(this@BookinfoActivity,"북마크 목록 추가가 실패하였습니다.", Toast.LENGTH_SHORT).show()
                                            val errorMessage = response.body()?.message
                                            // errorMessage 등을 활용하여 사용자에게 알림 또는 작업 수행
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


                            bookmarkButton.setImageResource(R.drawable.bookmarks_icon)
                            ApiClient.service.deleteBookmark(userId, bookId).enqueue(object : Callback<DeleteBookmarkResponse> {
                                override fun onResponse(call: Call<DeleteBookmarkResponse>, response: Response<DeleteBookmarkResponse>) {
                                    if (response.isSuccessful) {
                                        if (response.body()?.isSuccess == true) {
                                            // 북마크 삭제 성공
                                            Toast.makeText(this@BookinfoActivity,"북마크를 삭제하였습니다.", Toast.LENGTH_SHORT).show()
                                            val message = response.body()?.message
                                            // message 등을 활용하여 사용자에게 알림 또는 작업 수행
                                        } else {
                                            // 북마크 삭제 실패
                                            Toast.makeText(this@BookinfoActivity,"북마크 삭제가 실패하였습니다.", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this@BookinfoActivity, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BookIdResponse>, t: Throwable) {
                Toast.makeText(this@BookinfoActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()

            }
        })





















        renderView()







        val backButton = findViewById<ImageButton>(R.id.backButton)
        val bookcontent = findViewById<TextView>(R.id.bookcontent)
        val expandButton = findViewById<Button>(R.id.expandButton)



        // Back button click event
        backButton.setOnClickListener {
            finish()
        }



        // Bookmark button click event


        // Expand/Collapse button click event
        expandButton.setOnClickListener {
            if (bookcontent.maxLines == 5) {
                bookcontent.maxLines = Integer.MAX_VALUE  // 전체 텍스트 표시
                expandButton.text = "접기"  // 버튼 텍스트 변경
            } else {
                bookcontent.maxLines = 5  // 텍스트를 5줄까지만 표시
                expandButton.text = "자세히 보기"  // 버튼 텍스트 변경
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


    fun dialog(type: String){
        var dialog = AlertDialog.Builder(this)

        when(type) {
            "존재하지 않는 유저 id입니다." -> {
                dialog.setTitle("리뷰 조회 실패")
                dialog.setMessage("존재하지 않는 유저 id입니다.")
            }
            "존재하지 않는 책 id입니다." -> {
                dialog.setTitle("리뷰 조회 실패")
                dialog.setMessage("존재하지 않는 책 id입니다.")
            }

        }

        dialog.setPositiveButton("확인") { _, _ ->
            Log.d(TAG, "User acknowledged the dialog")
        }
        dialog.show()
    }






    private fun renderView() {

        binding.bookname.text = model?.title.orEmpty()
        binding.writer.text = model?.authors.toString()
        binding.publisher.text = model?.publisher.orEmpty()
        binding.publishdate.text = model?.datetime.orEmpty()
        binding.price.text = model?.price.toString()
        val isbnn = model?.isbn.toString().split(" ")
        val isbnnn = isbnn[0]
        binding.textIsbn.text = isbnnn
        binding.bookcontent.text = model?.contents.toString()




        Glide.with(binding.imageView.context)
            .load(model?.thumbnail.orEmpty())
            .into(binding.imageView)




    }




    }





