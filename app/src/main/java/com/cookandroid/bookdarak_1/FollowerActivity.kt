package com.cookandroid.bookdarak_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerActivity : AppCompatActivity() {

    private var userId: Int = -1

    class FollowerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val followerImage: ImageView = view.findViewById(R.id.follower_image)
        val followerName: TextView = view.findViewById(R.id.follower_name)
    }

    class FollowerAdapter(private val followers: List<Follower>) : RecyclerView.Adapter<FollowerViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.follower_item, parent, false)
            return FollowerViewHolder(view)
        }

        override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
            val follower = followers[position]
            val imageUrl = if (follower.followerImgUrl == "null") R.drawable.user_empty else follower.followerImgUrl

            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .placeholder(R.drawable.user_empty) // This will be shown until the image is loaded
                .error(R.drawable.user_empty)       // This will be shown if an error occurs during loading
                .override(200, 350)
                .into(holder.followerImage)

            holder.followerName.text = follower.followerName
        }


        override fun getItemCount() = followers.size
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_followerlist)

        userId = intent.getIntExtra("USER_ID", -1)
        val userName = intent.getStringExtra("USER_NAME")

        val userNameTextView = findViewById<TextView>(R.id.user_name)
        userNameTextView.text = "$userName 님의 팔로워"

        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.follower_list)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        getFollowers()
    }

    private fun getFollowers() {
        ApiClient.service.getFollowers(userId).enqueue(object : Callback<FollowerResponse> {
            override fun onResponse(
                call: Call<FollowerResponse>,
                response: Response<FollowerResponse>
            ) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    val followers = response.body()?.result
                    followers?.let {
                        updateRecyclerView(it)
                    }
                } else {
                    // Handle the error response
                }
            }

            override fun onFailure(call: Call<FollowerResponse>, t: Throwable) {
                // Handle the failure scenario
            }
        })
    }

    private fun updateRecyclerView(followers: List<Follower>) {
        val recyclerView = findViewById<RecyclerView>(R.id.follower_list)
        recyclerView.adapter = FollowerAdapter(followers)
    }
}
