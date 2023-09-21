package com.cookandroid.bookdarak_1

import android.content.Context
import android.content.Intent
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide


class BoardAdapter(private val context: Context, private val userId: Int) : RecyclerView.Adapter<BoardAdapter.ViewHolder>() { // userId 추가

    private val boardItems = mutableListOf<BoardItem>()

    fun setBoardItems(items: List<BoardItem>) {
        boardItems.clear()
        boardItems.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.board_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val boardItem = boardItems[position]
        holder.bind(boardItem)
    }

    override fun getItemCount(): Int = boardItems.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val commentRecyclerView: RecyclerView = view.findViewById(R.id.commentRecyclerView)
        private val commentAdapter = CommentAdapter(context)
        private val questionTextView: TextView = view.findViewById(R.id.questionTextView)
        private val bookImageView: ImageView = view.findViewById(R.id.bookImageView)
        private val bookTitleTextView: TextView = view.findViewById(R.id.bookTitleTextView)

        init {
            view.setOnClickListener {
                val boardId = boardItems[adapterPosition].boardId // boardId를 가져옵니다. 필드명은 데이터 클래스에 맞게 변경해야 합니다.
                Log.d("DEBUG_TAG", "Item clicked!")
                Log.d("DEBUG_TAG", "boardId: $boardId, userId: $userId")
                val intent = Intent(context, BoardDetailActivity::class.java)
                intent.putExtra("boardId", boardId)
                intent.putExtra("userId", userId)
                Log.d("DEBUG_TAG", "boardId: $boardId, userId: $userId")
                context.startActivity(intent)
            }
        }

        fun bind(boardItem: BoardItem) {
            questionTextView.text = boardItem.question
            Glide.with(context).load(boardItem.bookImg).into(bookImageView)
            commentRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            commentRecyclerView.adapter = commentAdapter
            bookTitleTextView.text = boardItem.bookname

            loadComments(boardItem.boardId)
        }

        private fun loadComments(boardId: Int) {
            ApiClient.service.getAllComments(boardId).enqueue(object : Callback<CommentResponse> {
                override fun onResponse(call: Call<CommentResponse>, response: Response<CommentResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            commentAdapter.setCommentItems(it.result.items.take(2))
                        }
                    }
                }

                override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                    // Handle error here
                }
            })
        }
    }
}
