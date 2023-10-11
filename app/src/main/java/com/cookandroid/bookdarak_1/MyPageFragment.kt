package com.cookandroid.bookdarak_1

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
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
    private lateinit var logoutTextView: TextView

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
        logoutTextView = view.findViewById(R.id.logout)
        logoutTextView.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        fetchUserReviews()

        fetchUserData()

        val bookmarkClickView = view.findViewById<TextView>(R.id.text_total_bookmark)
        val bookmarkNClickView = view.findViewById<TextView>(R.id.text_total_bookmark_n) // 여기서 참조를 가져옵니다.

        val openBookmarkActivity = View.OnClickListener {
            val intent = Intent(context, BookmarkActivity::class.java)
            intent.putExtra("USER_ID", userId) // 유저ID 정보 전달
            intent.putExtra("USER_NAME", userName.text.toString()) // 유저 닉네임 정보 전달
            startActivity(intent)
        }

        bookmarkClickView.setOnClickListener(openBookmarkActivity) // 클릭 리스너 설정
        bookmarkNClickView.setOnClickListener(openBookmarkActivity) // 'text_total_bookmark_n'에도 동일한 리스너 설정

        val followerClickView = view.findViewById<TextView>(R.id.text_total_following)
        val followerNClickView = view.findViewById<TextView>(R.id.text_total_following_n)

        val openFollowerActivity = View.OnClickListener {
            val intent = Intent(context, FollowerActivity::class.java)
            intent.putExtra("USER_ID", userId) // 유저ID 정보 전달
            intent.putExtra("USER_NAME", userName.text.toString()) // 유저 닉네임 정보 전달
            startActivity(intent)
        }

        followerClickView.setOnClickListener(openFollowerActivity) // 클릭 리스너 설정
        followerNClickView.setOnClickListener(openFollowerActivity) // 'text_total_following_n'에도 동일한 리스너 설정
        val deleteUserTextView: TextView = view.findViewById(R.id.deleteuser)
        deleteUserTextView.setOnClickListener {
            showDeleteConfirmationDialog()
        }


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
    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("정말 회원탈퇴하시겠습니까?")
            .setPositiveButton("확인") { dialog, _ ->
                deleteUser()
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    private fun deleteUser() {
        ApiClient.service.deleteUser(userId).enqueue(object : Callback<UserDeleteResponse> {
            override fun onResponse(call: Call<UserDeleteResponse>, response: Response<UserDeleteResponse>) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    // 회원 탈퇴 성공 -> 로그인 화면으로 이동
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()  // 현재 Activity 종료
                } else {
                    // 회원 탈퇴 실패 처리
                    Toast.makeText(context, response.body()?.message ?: "오류 발생", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserDeleteResponse>, t: Throwable) {
                Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("정말 로그아웃하시겠습니까?")
            .setPositiveButton("확인") { dialog, _ ->
                logoutUser() // 로그아웃 처리 함수 호출
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    private fun logoutUser() {
        // 로그인 화면으로 이동
        val intent = Intent(context, MainActivity::class.java) // 가정: 로그인 액티비티 이름이 LoginActivity임
        startActivity(intent)
        activity?.finish()  // 현재 Activity 종료
    }


}
