package com.cookandroid.bookdarak_1

import adapter.Bookinfo_ReviewAdapter
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.bookdarak_1.databinding.ActivityCertainBookreviewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class certain_bookreview : AppCompatActivity() {

    private lateinit var binding: ActivityCertainBookreviewBinding
    private var userId: Int = -1
    private var bookId: Int = -1
    val sort = "DESC"


    private lateinit var bookname : TextView
    private lateinit var bookinfo_reviewAdapter: Bookinfo_ReviewAdapter
    private lateinit var bookinfo_reviewrecyclerview2 : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_certain_bookreview)


        binding = ActivityCertainBookreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getIntExtra("USER_ID", -1)
        bookId = intent.getIntExtra("BOOK_ID", -1)
        Log.d("Certain_bookreview", "user,bookid: $userId, $bookId")

        bookname = findViewById(R.id.review_bookname)

        bookinfo_reviewAdapter = Bookinfo_ReviewAdapter(mutableListOf())
        bookinfo_reviewrecyclerview2 = findViewById(R.id.certain_book_review_reviewrecyclerview)
        bookinfo_reviewrecyclerview2.adapter = bookinfo_reviewAdapter
        val layoutManager = LinearLayoutManager(this)
        bookinfo_reviewrecyclerview2.layoutManager = layoutManager

        ApiClient.service.getBookDetail(bookId)
            .enqueue(object : Callback<BookResponse> {
                override fun onResponse(
                    call: Call<BookResponse>,
                    response: Response<BookResponse>
                ) {
                    if (response.isSuccessful) {
                        val result_name = response.body()?.result?.name
                        Log.d("Certain_bookreview", "certain_bookreview_name: $result_name")

                        binding.reviewBookname.text = result_name

                    } else {
                        // Handle error
                    }
                }

                override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                    // Handle failure
                }
            })

        ApiClient.service.getBookReviews(bookId,sort).enqueue(object : Callback<ReviewListResponse> {
            override fun onResponse(call: Call<ReviewListResponse>, response: Response<ReviewListResponse>) {
                if (response.isSuccessful) {
                    val results = response.body()?.result?.items
                    Log.d("certain_bookreview", "certain_results: $results")

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


    }
}