package com.cookandroid.bookdarak_1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cookandroid.bookdarak_1.databinding.ActivityUserpageBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Userpage : AppCompatActivity() {

    private lateinit var binding: ActivityUserpageBinding
    private var userId: Int = -1

    private lateinit var userName : TextView
    private lateinit var userName2 : TextView
    private lateinit var userIntro : TextView
    private lateinit var totalReview : TextView
    private lateinit var totalBookmark: TextView
    private lateinit var totalFollowing: TextView
    private lateinit var imageUserLibrary1: ImageView
    private lateinit var imageUserLibrary2: ImageView
    private lateinit var imageUserLibrary3: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userpage)
        binding = ActivityUserpageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getIntExtra("USER_ID", -1)

        userName = findViewById(R.id.text_user_nickname)
        userName2 = findViewById(R.id.text_user_nickname2)
        userIntro = findViewById(R.id.text_user_introduce)
        totalReview = findViewById(R.id.text_user_total_review)
        totalBookmark = findViewById(R.id.text_user_total_bookmark)
        totalFollowing = findViewById(R.id.text_user_total_following)
        imageUserLibrary1 = findViewById(R.id.image_user_library1)
        imageUserLibrary2 = findViewById(R.id.image_user_library2)
        imageUserLibrary3 = findViewById(R.id.image_user_library3)


        //val followerImage: ImageView = view.findViewById(R.id.follower_image)





        fetchUserReviews()

        fetchUserData()

        val bookmarkClickView = findViewById<TextView>(R.id.text_user_total_bookmark)
        val bookmarkNClickView = findViewById<TextView>(R.id.text_user_total_bookmark_n) // 여기서 참조를 가져옵니다.

        val openBookmarkActivity = View.OnClickListener {
            val intent = Intent(this@Userpage, BookmarkActivity::class.java)
            intent.putExtra("USER_ID", userId) // 유저ID 정보 전달
            intent.putExtra("USER_NAME", userName.text.toString()) // 유저 닉네임 정보 전달
            startActivity(intent)
        }

        bookmarkClickView.setOnClickListener(openBookmarkActivity) // 클릭 리스너 설정
        bookmarkNClickView.setOnClickListener(openBookmarkActivity) // 'text_total_bookmark_n'에도 동일한 리스너 설정

        val followerClickView = findViewById<TextView>(R.id.text_user_total_following)
        val followerNClickView = findViewById<TextView>(R.id.text_user_total_following_n)

        val openFollowerActivity = View.OnClickListener {
            val intent = Intent(this@Userpage, FollowerActivity::class.java)
            intent.putExtra("USER_ID", userId) // 유저ID 정보 전달
            intent.putExtra("USER_NAME", userName.text.toString()) // 유저 닉네임 정보 전달
            startActivity(intent)


    }
        followerClickView.setOnClickListener(openFollowerActivity) // 클릭 리스너 설정
        followerNClickView.setOnClickListener(openFollowerActivity) // 'text_total_following_n'에도 동일한 리스너 설정



}

    private fun fetchUserReviews() {
        ApiClient.service.getUserReviews(userId, "N").enqueue(object : Callback<UserReviewResponse> {
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
                    .into(imageUserLibrary1)
                // Assuming there are at least 3 reviews
                if (it.size > 1) {
                    Glide.with(this)
                        .load(it[1].bookImgUrl)
                        .into(imageUserLibrary2)
                }
                if (it.size > 2) {
                    Glide.with(this)
                        .load(it[2].bookImgUrl)
                        .into(imageUserLibrary3)
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


        }
    }

}
