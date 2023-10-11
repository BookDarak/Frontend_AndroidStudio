package com.cookandroid.bookdarak_1

import adapter.ReviewAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response






class ReviewFragment : Fragment(), ReviewAdapter.OnThumbsUpClickListener,ReviewAdapter.OnBookImageClickListener {
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

        userId = arguments?.getInt("USER_ID", -1) ?: -1

        return inflater.inflate(R.layout.fragment_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reviewListView = view.findViewById(R.id.review_list_view)
        reviewAdapter = ReviewAdapter(mutableListOf()) // Initialize ReviewAdapter instance
        reviewAdapter.setOnThumbsUpClickListener(this)
        reviewAdapter.setOnBookImageClickListener(this)


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
        Log.d("ReviewFragment", "onThumbsUpClick - userId: $userId, reviewId: $reviewId")
        // Handle thumbs-up button click here
        // You can make an API call to update the like count on the server
        // Then, refresh the data by calling loadReviewData() again
        // Example:

        ApiClient.service.checkRecommendStatus(userId,reviewId )
            .enqueue(object : Callback<RecommendStatusResponse> {
                override fun onResponse(
                    call: Call<RecommendStatusResponse>,
                    response: Response<RecommendStatusResponse>
                ) {
                    if (response.isSuccessful) {
                        val torf = response.body()?.result
                        Log.d("ReviewFragment", "Review_torf: $torf")


                        if ( torf =="false"){
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
                                            Toast.makeText(context, "요약서평을 추천했습니다", Toast.LENGTH_SHORT).show()

                                            ApiClient.service.getRecommendCountByReviewId(reviewId)
                                                .enqueue(object : Callback<RecommendCountResponse> {
                                                    override fun onResponse(
                                                        call: Call<RecommendCountResponse>,
                                                        response: Response<RecommendCountResponse>
                                                    ) {
                                                        if (response.isSuccessful) {
                                                            val result_get = response.body()?.result
                                                            Log.d("ReviewFragment", "Review_Count_get: $result_get")

                                                            ApiClient.service.getPublicSummaryReviews(sort)
                                                                .enqueue(object : Callback<ReviewSummaryResponse> {
                                                                    override fun onResponse(
                                                                        call: Call<ReviewSummaryResponse>,
                                                                        response: Response<ReviewSummaryResponse>
                                                                    ) {
                                                                        if (response.isSuccessful) {
                                                                            val results = response.body()?.result?.items
                                                                            Log.d("ReviewFragment", "Review_frag_results2: $results")

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
                        }else if ( torf =="true"){
                            ApiClient.service.deleteRecommendation(userId, reviewId)
                                .enqueue(object : Callback<RecommendDeleteResponse> {
                                    override fun onResponse(
                                        call: Call<RecommendDeleteResponse>,
                                        response: Response<RecommendDeleteResponse>
                                    ) {
                                        if (response.isSuccessful) {
                                            val result = response.body()?.result
                                            Log.d("ReviewFragment", "Review_Count: $result")
                                            // Handle success
                                            // Refresh data by calling loadReviewData()
                                            Toast.makeText(context, "요약서평 추천을 삭제했습니다", Toast.LENGTH_SHORT).show()

                                            ApiClient.service.getRecommendCountByReviewId(reviewId)
                                                .enqueue(object : Callback<RecommendCountResponse> {
                                                    override fun onResponse(
                                                        call: Call<RecommendCountResponse>,
                                                        response: Response<RecommendCountResponse>
                                                    ) {
                                                        if (response.isSuccessful) {
                                                            val result_get = response.body()?.result
                                                            Log.d("ReviewFragment", "Review_Count_get: $result_get")

                                                            ApiClient.service.getPublicSummaryReviews(sort)
                                                                .enqueue(object : Callback<ReviewSummaryResponse> {
                                                                    override fun onResponse(
                                                                        call: Call<ReviewSummaryResponse>,
                                                                        response: Response<ReviewSummaryResponse>
                                                                    ) {
                                                                        if (response.isSuccessful) {
                                                                            val results = response.body()?.result?.items
                                                                            Log.d("ReviewFragment", "Review_frag_results2: $results")

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

                                    override fun onFailure(call: Call<RecommendDeleteResponse>, t: Throwable) {
                                        // Handle failure
                                    }
                                })
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

                override fun onFailure(call: Call<RecommendStatusResponse>, t: Throwable) {
                    Log.e("ReviewFragment", "Error fetching data: ${t.message}")
                }
            })


    }

    override fun onBookImageClick(bookId: Int) {
        Log.d("ReviewFragment", "onBookImageClick - userId: $userId, reviewId: $bookId")
        // Handle thumbs-up button click here
        // You can make an API call to update the like count on the server
        // Then, refresh the data by calling loadReviewData() again
        // Example:

        ApiClient.service.getBookDetail(bookId)
            .enqueue(object : Callback<BookResponse> {
                override fun onResponse(
                    call: Call<BookResponse>,
                    response: Response<BookResponse>
                ) {
                    if (response.isSuccessful) {
                        val result_isbn = response.body()?.result?.isbn
                        Log.d("ReviewFragment", "Review_isbn_get: $result_isbn")
                        val intent = Intent(requireContext(), BookinfoActivity2::class.java)


                        intent.putExtra("isbn_of_home",result_isbn)
                        intent.putExtra("USER_ID", userId)




                        startActivity(intent)

                    } else {
                        // Handle error
                    }
                }

                override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                    // Handle failure
                }
            })



    }
}
