package com.cookandroid.bookdarak_1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.cookandroid.bookdarak_1.databinding.BoardDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardDetailActivity : AppCompatActivity() {
    private lateinit var binding: BoardDetailBinding
    private val commentAdapter = CommentAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BoardDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView
        binding.commentRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.commentRecyclerView.adapter = commentAdapter


        val boardId = intent.getIntExtra("boardId", -1)
        val userId = intent.getIntExtra("userId", -1)
        Log.d("DEBUG_TAG", "boardId: $boardId, userId: $userId")

        if (boardId == -1 || userId == -1) {
            Toast.makeText(this, "Invalid board or user ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Load board details
        loadBoardDetails(boardId)

        // Load comments
        loadComments(boardId)

        binding.backButton.setOnClickListener {
            val intent = Intent(this, BoardActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.submitCommentButton.setOnClickListener {
            val content = binding.commentEditText.text.toString().trim()

            if (content.isNotEmpty()) {
                postComment(boardId, userId, content)
            } else {
                Toast.makeText(this, "Please enter a comment", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadBoardDetails(boardId: Int) {
        ApiClient.service.getBoardDetail(boardId).enqueue(object : Callback<BoardDetailResponse> {
            override fun onResponse(
                call: Call<BoardDetailResponse>,
                response: Response<BoardDetailResponse>
            ) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    val result = response.body()?.result
                    binding.questionTextView.text = result?.question
                    binding.bookTitleTextView.text = result?.bookname

                    // Load the book image using Glide
                    if (!result?.bookImg.isNullOrEmpty()) {
                        Glide.with(this@BoardDetailActivity).load(result?.bookImg).into(binding.bookImageView)
                    } else {

                    }

                } else {
                    Toast.makeText(this@BoardDetailActivity, "Failed to load board details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BoardDetailResponse>, t: Throwable) {
                Toast.makeText(this@BoardDetailActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun loadComments(boardId: Int) {
        ApiClient.service.getAllComments(boardId).enqueue(object : Callback<CommentResponse> {
            override fun onResponse(
                call: Call<CommentResponse>,
                response: Response<CommentResponse>
            ) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    val comments = response.body()?.result?.items
                    if (!comments.isNullOrEmpty()) {
                        commentAdapter.setCommentItems(comments)
                    }
                } else {
                    Toast.makeText(this@BoardDetailActivity, "Failed to load comments", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                Toast.makeText(this@BoardDetailActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun postComment(boardId: Int, userId: Int, content: String) {
        val requestBody = CommentPostBody(content)
        ApiClient.service.postComment(boardId, userId, requestBody).enqueue(object : Callback<CommentPostResponse> {
            override fun onResponse(
                call: Call<CommentPostResponse>,
                response: Response<CommentPostResponse>
            ) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    Toast.makeText(this@BoardDetailActivity, "Comment posted successfully", Toast.LENGTH_SHORT).show()
                    binding.commentEditText.text.clear()
                    loadComments(boardId) // Refresh comments
                } else {
                    Toast.makeText(this@BoardDetailActivity, "Failed to post comment", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CommentPostResponse>, t: Throwable) {
                Toast.makeText(this@BoardDetailActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
