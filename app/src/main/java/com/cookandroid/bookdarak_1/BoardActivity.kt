package com.cookandroid.bookdarak_1

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BoardActivity : AppCompatActivity() {

    private lateinit var boardAdapter: BoardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        val recyclerView = findViewById<RecyclerView>(R.id.boardRecyclerView)
        boardAdapter = BoardAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = boardAdapter

        val verticalSpacing = resources.getDimensionPixelSize(R.dimen.your_vertical_spacing)
        recyclerView.addItemDecoration(VerticalSpaceItemDecoration(verticalSpacing))

        loadBoards()
    }

    private fun loadBoards() {
        ApiClient.service.getAllBoards().enqueue(object : Callback<BoardResponse> {
            override fun onResponse(call: Call<BoardResponse>, response: Response<BoardResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        boardAdapter.setBoardItems(it.result.items)
                    }
                }
            }

            override fun onFailure(call: Call<BoardResponse>, t: Throwable) {
                // Handle error here
            }
        })
    }
    class VerticalSpaceItemDecoration(private val verticalSpaceHeight: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
        ) {
            outRect.bottom = verticalSpaceHeight
        }
    }
}
