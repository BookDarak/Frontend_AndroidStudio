package com.cookandroid.bookdarak_1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageFragment : Fragment() {
    private var userId: Int = -1
    private lateinit var profileImage: ImageView
    private lateinit var userName: TextView
    private lateinit var userIntro: TextView
    private lateinit var totalReview: TextView
    private lateinit var totalBookmark: TextView
    private lateinit var totalFollowing: TextView
    private lateinit var imageMyLibrary1: ImageView
    private lateinit var imageMyLibrary2: ImageView
    private lateinit var imageMyLibrary3: ImageView

    companion object {
        fun newInstance(userId: Int): MyPageFragment {
            val fragment = MyPageFragment()
            val args = Bundle()
            args.putInt("USER_ID", userId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getInt("USER_ID", -1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_page, container, false)

        profileImage = view.findViewById(R.id.imageView)
        userName = view.findViewById(R.id.text_username)
        userIntro = view.findViewById(R.id.text_introduce)
        totalReview = view.findViewById(R.id.text_total_review)
        totalBookmark = view.findViewById(R.id.text_total_bookmark)
        totalFollowing = view.findViewById(R.id.text_total_following)
        imageMyLibrary1 = view.findViewById(R.id.image_mylibrary1)
        imageMyLibrary2 = view.findViewById(R.id.image_mylibrary2)
        imageMyLibrary3 = view.findViewById(R.id.image_mylibrary3)

        fetchUserReviews()

        fetchUserData()

        return view
    }

    private fun fetchUserReviews() {
        ApiClient.service.getUserReviews(userId, "Y").enqueue(object : Callback<UserReviewResponse> {
            override fun onResponse(call: Call<UserReviewResponse>, response: Response<UserReviewResponse>) {
                if (response.isSuccessful) {
                    val reviews = response.body()?.result?.items
                    updateReviewUI(reviews)
                } else {
                    // Handle error
                }
            }

            override fun onFailure(call: Call<UserReviewResponse>, t: Throwable) {
                // Handle error
            }
        })
    }

    private fun updateReviewUI(reviews: List<UserReviewResponse.ReviewListResult.ReviewItem>?) {
        reviews?.let {
            if (it.isNotEmpty()) {
                Glide.with(this)
                    .load(it[0].bookImgUrl)
                    .into(imageMyLibrary1)
                // Assuming there are at least 3 reviews
                if (it.size > 1) {
                    Glide.with(this)
                        .load(it[1].bookImgUrl)
                        .into(imageMyLibrary2)
                }
                if (it.size > 2) {
                    Glide.with(this)
                        .load(it[2].bookImgUrl)
                        .into(imageMyLibrary3)
                }
            }
        }
    }

    private fun fetchUserData() {
        ApiClient.service.getUserInfo(userId).enqueue(object : Callback<UserInfoResponse> {
            override fun onResponse(call: Call<UserInfoResponse>, response: Response<UserInfoResponse>) {
                if (response.isSuccessful) {
                    val user = response.body()?.result
                    updateUI(user)
                } else {
                    // Handle error
                }
            }

            override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                // Handle error
            }
        })
    }

    private fun updateUI(user: UserInfoResponse.UserResult?) {
        user?.let {
            userName.text = it.name
            userIntro.text = it.introduction
            totalReview.text = it.reviewCount.toString()
            totalBookmark.text = it.bookmarkCount.toString()
            totalFollowing.text = it.followCount.toString()

            it.profileUrl?.let { url ->
                Glide.with(this)
                    .load(url)
                    .into(profileImage)
            }
        }
    }
}
