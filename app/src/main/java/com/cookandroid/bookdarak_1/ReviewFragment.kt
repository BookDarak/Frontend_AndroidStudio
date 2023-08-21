package com.cookandroid.bookdarak_1

import adapter.ReviewAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import model.Review

class ReviewFragment : Fragment() {
/*
    private lateinit var reviewAdapter: ReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_review, container, false)

        val reviewId = intent.getIntExtra("REVIEW_ID", -1) // 이전 액티비티에서 전달한 리뷰 ID

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
                    Toast.makeText(this@ReviewFragment, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ReviewDetailResponse>, t: Throwable) {
                Toast.makeText(this@ReviewFragment, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
        )

        // Set up RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.review_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        reviewAdapter = ReviewAdapter(reviews)
        recyclerView.adapter = reviewAdapter

        return view
    }

 */

    private lateinit var reviewAdapter: ReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_review, container, false)

        // Prepare reviews
        val reviews = listOf(
            Review("다락 about 'AI 미래보고서'" , "가독성이 좋다", ),
            Review("무명 about '인간실격'", "사실 보통이라는게 사회 전반적인 합의가 아니라 내 주변의 몇몇 사람들만 합의한 내용은 아닐까라는 생각도 든다.\n" +
                    "세상의 보통이 아니라, 일부 개인들의 보통은 아닐까? 아니 개인들이 모여 집단을 이루고 그 집단이 모여 세상을 이루니 집단들의 보통이라고 해야 할까. 오늘도 보통은 참 어렵다."),
            Review("박지한 about '미움받을 용기'", "목적론, 트라우마란 존재하지 않는다. - 경험에 의해 결정되는 것이 아니라, 경험에 부여한 의미에 따라 자신을 결정하는 것이다. 우리는 과거의 경험에 '어떤의미를 부여하는가'에 따라 자신의 삶을 결정한다. 인생이란 누군가 정해주는 것이 아니라 스스로 선택하는 것이다. 우리는 어떠한 ' 목적'을 따라 산다.")
        )

        // Set up RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.review_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        reviewAdapter = ReviewAdapter(reviews)
        recyclerView.adapter = reviewAdapter

        return view
    }
}