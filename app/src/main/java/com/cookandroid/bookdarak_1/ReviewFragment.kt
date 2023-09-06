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




    class ReviewFragment : Fragment() {

        lateinit var reviewRecyclerView: RecyclerView // 리사이클러뷰 어댑터
        val sort = "DESC"
        private lateinit var ReviewAdapter: ReviewAdapter
        //private val apiService = ApiService.create() // Retrofit을 사용하여 API 서비스 생성

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

            ReviewAdapter = ReviewAdapter(mutableListOf()) // ReviewAdapter 인스턴스 초기화

            reviewRecyclerView.layoutManager = LinearLayoutManager(context)
            reviewRecyclerView.adapter = ReviewAdapter



        }

        private fun loadReviewData() {
            ApiClient.service.getPublicSummaryReviews(sort).enqueue(object : Callback<ReviewSummaryResponse> {
                override fun onResponse(call: Call<ReviewSummaryResponse>, response: Response<ReviewSummaryResponse>) {
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


                    } else {
                        Log.e("ReviewFragment", "Server returned error: ${response.code()} - ${response.message()}")
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
    }


/*
companion object {
    fun newInstance(userId: Int): ReviewFragment {
        val fragment = ReviewFragment()
        val args = Bundle()
        //args.putInt("USER_ID", userId)
        fragment.arguments = args
        return fragment
    }
}




private lateinit var reviewAdapter: ReviewAdapter

override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {
    val view = inflater.inflate(R.layout.fragment_review, container, false)

    val review_recycler_view = view.findViewById<R.id.review>()


    ApiClient.service.getPublicSummaryReviews(sort).enqueue(object : Callback<ReviewSummaryResponse> {
        override fun onResponse(call: Call<ReviewSummaryResponse>, response: Response<ReviewSummaryResponse>) {
            if (response.isSuccessful) {
                val results = response.body()?.result
                //updateCalendarWithEvents(results)
            } else {
                Log.e("ReviewFragment", "Server returned error: ${response.code()} - ${response.message()}")
                response.body()?.let {
                    Log.e("ReviewFragment", "Error code: ${it.code} - ${it.message}")
                }
            }
        }

        override fun onFailure(call: Call<ReviewSummaryResponse>, t: Throwable) {
            Log.e("ReviewrFragment", "Error fetching data: ${t.message}")
        }
    })

    //return veiw
}
}

    /*
    val reviews = listOf(

        Review("무명 about '인간실격'", "사실 보통이라는게 사회 전반적인 합의가 아니라 내 주변의 몇몇 사람들만 합의한 내용은 아닐까라는 생각도 든다.\n" +
                "세상의 보통이 아니라, 일부 개인들의 보통은 아닐까? 아니 개인들이 모여 집단을 이루고 그 집단이 모여 세상을 이루니 집단들의 보통이라고 해야 할까. 오늘도 보통은 참 어렵다."),
        Review("박지한 about '미움받을 용기'", "목적론, 트라우마란 존재하지 않는다. - 경험에 의해 결정되는 것이 아니라, 경험에 부여한 의미에 따라 자신을 결정하는 것이다. 우리는 과거의 경험에 '어떤의미를 부여하는가'에 따라 자신의 삶을 결정한다. 인생이란 누군가 정해주는 것이 아니라 스스로 선택하는 것이다. 우리는 어떠한 ' 목적'을 따라 산다.")
    )

     */

    // Set up RecyclerView
/*
    val recyclerView = view.findViewById<RecyclerView>(R.id.review_recycler_view)
    recyclerView.layoutManager = LinearLayoutManager(activity)
    reviewAdapter = ReviewAdapter(reviews)
    recyclerView.adapter = reviewAdapter

    return view
}

*/

 */
