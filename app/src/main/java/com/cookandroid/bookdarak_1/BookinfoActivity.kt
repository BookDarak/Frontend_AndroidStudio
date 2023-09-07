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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cookandroid.bookdarak_1.data.api.FindBookAPI
import com.cookandroid.bookdarak_1.data.model.FBook
import com.cookandroid.bookdarak_1.data.model.SearchResponse
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
    private var isbn_of_home: String = "-1"
    val TAG: String = "BookinfoActivity"
    lateinit var bookinforeviewRecyclerView: RecyclerView // RecyclerView adapter
    val sort = "DESC"
    private lateinit var bookinfo_reviewAdapter: Bookinfo_ReviewAdapter
    private lateinit var bookRecyclerViewAdapter: BookSearchViewHolder
    private lateinit var bookService: FindBookAPI












    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //db = getAppDatabase(this)
        binding = ActivityBookinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)



        bookinforeviewRecyclerView = findViewById(R.id.bookinfo_review_recycler_view)

        bookinfo_reviewAdapter = Bookinfo_ReviewAdapter(mutableListOf()) // Initialize ReviewAdapter instance

        bookinforeviewRecyclerView.layoutManager = LinearLayoutManager(this)
        bookinforeviewRecyclerView.adapter = bookinfo_reviewAdapter

        // 다른 액티비티에서 ID를 받아옴
        val receivedIntent = intent


        userId = receivedIntent.getIntExtra("USER_ID", -1)
        isbn_of_home = receivedIntent.getStringExtra("isbn_of_home").toString()
        //bookId = intent.getIntExtra("BOOK_ID", -1)

        Log.d(TAG, "Received USER_ID: $userId")

        model = intent.getParcelableExtra("bookModel")

        val writerText = model?.authors.toString()
        val booktitle = model?.title.toString()
        val authorList = model?.authors ?: emptyList()
        //val writerList = listOf("Alice", "Bob", "Charlie")
        //val writerString = writerList?.joinToString(", ") ?: ""
        val isbn = model?.isbn.toString()


        val image = model?.thumbnail.toString()

        //val writerList: List<String> = model?.authors ?: emptyList()

        Log.d(TAG, "rbookidrequest: $booktitle,$isbn,$image,$authorList")
        val BookIdrequest = BookIdRequest(booktitle, authorList, isbn, image) // gender 추가
        Log.d(TAG, "rbookidrequest_2: $BookIdrequest")

        if (isbn_of_home != "-1") {

            initBookService()

            bookService.FindBook(isbn_of_home).enqueue(object : Callback<SearchResponse> {override fun onResponse(
                    call: Call<SearchResponse>, response: Response<SearchResponse>) {
                    if (response.isSuccessful.not()) {
                        return
                    }


                val results_of_isbn_of_home = response.body()?.documents

                Log.d("BookinfoActivity", "results_of_isbn_of_home: $results_of_isbn_of_home")
                renderView()

                   // FindFragment.bookRecyclerViewAdapter.submitList(response.body()?.documents.orEmpty()) // 새 리스트로 갱신
                    //어댑터에 북리스트전달하여 북리사이클러뷰그림
                }

                // 실패.
                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    //hideHistoryView()
                    Log.e(TAG, t.toString())
                }
            })

            }





        ApiClient.service.bookId(BookIdrequest).enqueue(object: Callback<BookIdResponse> {
            override fun onResponse(call: Call<BookIdResponse>, response: Response<BookIdResponse>) {
                if (response.isSuccessful  && response.body()?.isSuccess == true) {
                    Log.d(TAG, "rbookidrequest_3: $authorList")
                    //val bookId = response.body()?.result?.bookId ?: -1
                    val bookId = response.body()?.result?.id ?: -1  // <-- 'bookId'를 'id'로 수정
                    Log.d(TAG, "wwbookID: $bookId")



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


                    val writeButton: Button = findViewById(R.id.writebutton)
                    writeButton.setOnClickListener {

                        val intent = Intent(this@BookinfoActivity, writingreview::class.java)
                        intent.putExtra("bookModel", model)
                        intent.putExtra("USER_ID", userId) // Passing userId to writingreview activity
                        intent.putExtra("BOOK_ID", bookId)
                        Log.d(TAG, "BookinfoActivity_user and bookID: $userId, $bookId")
                        startActivity(intent)




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








    private fun renderView() {

        binding.bookname.text = model?.title.orEmpty()
        binding.writer.text = model?.authors.toString()
        binding.publisher.text = model?.publisher.orEmpty()
        binding.publishdate.text = model?.datetime.orEmpty()
        binding.price.text = model?.price.toString()
        binding.textIsbn.text = model?.isbn.toString()
        binding.bookcontent.text = model?.contents.toString()




        Glide.with(binding.imageView.context)
            .load(model?.thumbnail.orEmpty())
            .into(binding.imageView)




    }




    }





