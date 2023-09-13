package com.cookandroid.bookdarak_1

import adapter.ReviewAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response




class ReviewFragment : Fragment(),ReviewAdapter.OnThumbsUpClickListener {




        lateinit var reviewRecyclerView: RecyclerView // 리사이클러뷰 어댑터
        val sort = "DESC"
        private lateinit var ReviewAdapter: ReviewAdapter
        private var userId: Int = -1
        //private val apiService = ApiService.create() // Retrofit을 사용하여 API 서비스 생성
        companion object {
            fun newInstance(userId: Int): ReviewFragment {
                val fragment = ReviewFragment()
                val args = Bundle()
                args.putInt("USER_ID", userId)
                fragment.arguments = args

                return fragment
            }

        }


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)


            // 서평 데이터를 가져와 리사이클러뷰에 표시
            loadReviewData()
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_review, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            reviewRecyclerView = view.findViewById(R.id.review_recycler_view)

            ReviewAdapter = ReviewAdapter(mutableListOf())// ReviewAdapter 인스턴스 초기화
            ReviewAdapter.setOnThumbsUpClickListener(this)

            reviewRecyclerView.layoutManager = LinearLayoutManager(context)
            reviewRecyclerView.adapter = ReviewAdapter


        }



        private fun loadReviewData() {
            ApiClient.service.getPublicSummaryReviews(sort)
                .enqueue(object : Callback<ReviewSummaryResponse> {
                    override fun onResponse(
                        call: Call<ReviewSummaryResponse>,
                        response: Response<ReviewSummaryResponse>
                    ) {
                        if (response.isSuccessful) {
                            val results = response.body()?.result?.items
                            //Log.d("ReviewFragment", "Review_frag_results: $results")
                            //updateCalendarWithEvents(results)
                            //val reviewSummaryResponse = response.body()
                            Log.d("ReviewFragment", "Review_frag_results: $results")


                            // 데이터가 정상적으로 받아졌을 때 어댑터에 데이터 설정
                            results?.let {
                                ReviewAdapter.submitData(it)
                            }

                            //onThumbsUpClick(ReviewAdapter.setOnThumbsUpClickListener())


                        } else {
                            Log.e(
                                "ReviewFragment",
                                "Server returned error: ${response.code()} - ${response.message()}"
                            )
                            response.body()?.let {
                                Log.e("ReviewFragment", "Error code: ${it.code} - ${it.message}")
                            }
                        }
                    }

                    override fun onFailure(call: Call<ReviewSummaryResponse>, t: Throwable) {
                        Log.e("ReviewFragment", "Error fetching data: ${t.message}")
                    }
                })
        }

    override fun onThumbsUpClick(reviewId: Int) {
        // Handle thumbs-up button click here
        // You can make an API call to update the like count on the server
        // Then, refresh the data by calling loadReviewData() again
        // Example:
        ApiClient.service.addRecommendation(userId, reviewId)
            .enqueue(object : Callback<RecommendResponse> {
                override fun onResponse(
                    call: Call<RecommendResponse>,
                    response: Response<RecommendResponse>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()?.result
                        Log.d("ReviewFragment", "Review_Count: $result")
                        // Handle success
                        // Refresh data by calling loadReviewData()

                        ApiClient.service.getRecommendCountByReviewId(reviewId)
                            .enqueue(object : Callback<RecommendCountResponse> {
                                override fun onResponse(
                                    call: Call<RecommendCountResponse>,
                                    response: Response<RecommendCountResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        // Handle success
                                        // Refresh data by calling loadReviewData()

                                    } else {
                                        // Handle error
                                    }
                                }

                                override fun onFailure(call: Call<RecommendCountResponse>, t: Throwable) {
                                    // Handle failure
                                }
                            })

                    } else {
                        // Handle error
                    }
                }

                override fun onFailure(call: Call<RecommendResponse>, t: Throwable) {
                    // Handle failure
                }
            })
    }
    }


