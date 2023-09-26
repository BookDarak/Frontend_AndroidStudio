package com.cookandroid.bookdarak_1

import adapter.ReviewAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response






class ReviewFragment : Fragment(), ReviewAdapter.OnThumbsUpClickListener {
    lateinit var reviewListView: ListView
    private lateinit var reviewAdapter: ReviewAdapter
    val sort = "DESC"
    private var userId: Int = -1

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
        // Get book review data and display it in ListView
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

        reviewListView = view.findViewById(R.id.review_list_view)
        reviewAdapter = ReviewAdapter(mutableListOf()) // Initialize ReviewAdapter instance
        reviewAdapter.setOnThumbsUpClickListener(this)
        reviewListView.adapter = reviewAdapter
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
                        Log.d("ReviewFragment", "Review_frag_results: $results")

                        // Set data to adapter when data is received normally
                        results?.let {
                            reviewAdapter.submitData(it)
                        }
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
                        Log.d("ReviewFragment", "추천실패")// Handle error
                    }
                }

                override fun onFailure(call: Call<RecommendResponse>, t: Throwable) {
                    // Handle failure
                }
            })
    }
}
