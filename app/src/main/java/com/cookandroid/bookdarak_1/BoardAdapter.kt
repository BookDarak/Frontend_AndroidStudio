package com.cookandroid.bookdarak_1

import android.content.Context
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


class BoardAdapter(private val context: Context) : RecyclerView.Adapter<BoardAdapter.ViewHolder>() {

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
        // board_item.xml 내의 뷰 참조
        private val questionTextView: TextView = view.findViewById(R.id.questionTextView)
        private val bookImageView: ImageView = view.findViewById(R.id.bookImageView)

        fun bind(boardItem: BoardItem) {
            // 질문 텍스트 설정
            questionTextView.text = boardItem.question // 예시 필드명, 실제 데이터 클래스의 필드명으로 변경 필요

            // 책 이미지 설정 (Glide 라이브러리 사용 예시)
            Glide.with(context).load(boardItem.bookImg).into(bookImageView) // 예시 필드명, 실제 데이터 클래스의 필드명으로 변경 필요

            commentRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            commentRecyclerView.adapter = commentAdapter

            loadComments(boardItem.boardId)
        }

        private fun loadComments(boardId: Int) {
            ApiClient.service.getAllComments(boardId).enqueue(object : Callback<CommentResponse> {
                override fun onResponse(call: Call<CommentResponse>, response: Response<CommentResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            commentAdapter.setCommentItems(it.result.items.take(3))  // 최대 3개만 가져오도록 함
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
