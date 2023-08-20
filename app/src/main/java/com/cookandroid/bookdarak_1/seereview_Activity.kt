package com.cookandroid.bookdarak_1

import android.os.Bundle
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class seereview_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seereview)

        val reviewId = intent.getIntExtra("reviewId", -1) // 이전 액티비티에서 전달한 리뷰 ID

        // 리뷰 세부 정보 조회
        ApiClient.service.getReviewDetail(reviewId).enqueue(object :
            Callback<ReviewDetailResponse> {
            override fun onResponse(call: Call<ReviewDetailResponse>, response: Response<ReviewDetailResponse>) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    val reviewDetail = response.body()?.result


                    // Display review details on the UI
                    val bookTitleTextView = findViewById<TextView>(R.id.text_writingreview_booktitle)
                    val isbnTextView = findViewById<TextView>(R.id.text_writingreview_isbn)
                    val ratingBar = findViewById<RatingBar>(R.id.see_ratingbar)
                    val startDateTextView = findViewById<TextView>(R.id.startday)
                    val endDateTextView = findViewById<TextView>(R.id.finishday)
                    val reviewContentTextView = findViewById<TextView>(R.id.text_review)
                    val impressiveTextView = findViewById<TextView>(R.id.text_impressive)

                    // Populate UI elements with review details
                    //bookTitleTextView.text = model?.title.orEmpty() // Set book title
                    //isbnTextView.text = model?.isbn.orEmpty() // Set ISBN
                    ratingBar.rating = reviewDetail?.rating ?: 0.0f // Set rating
                    startDateTextView.text = reviewDetail?.startDate.orEmpty() // Set start date
                    endDateTextView.text = reviewDetail?.endDate.orEmpty() // Set end date
                    reviewContentTextView.text = reviewDetail?.content.orEmpty() // Set review content
                    impressiveTextView.text = reviewDetail?.phrase.orEmpty() // Set impressive phrase
                } else {
                    Toast.makeText(this@seereview_Activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ReviewDetailResponse>, t: Throwable) {
                Toast.makeText(this@seereview_Activity, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}


