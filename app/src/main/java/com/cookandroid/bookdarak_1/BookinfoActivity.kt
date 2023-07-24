package com.cookandroid.bookdarak_1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_bookinfo.*

class BookinfoActivity: AppCompatActivity() {
    private var bookmarked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookinfo)

        writebutton.setOnClickListener({
            val intent = Intent(this, writingreview::class.java)
            startActivity(intent)
        })

        val backButton = findViewById<ImageButton>(R.id.backButton)
        val bookmarkButton = findViewById<ImageButton>(R.id.bookmarkButton)
        val content = findViewById<TextView>(R.id.content)
        val expandButton = findViewById<Button>(R.id.expandButton)

        // Back button click event
        backButton.setOnClickListener {
            finish()
        }

        // Bookmark button click event
        var isBookmarked = false
        bookmarkButton.setOnClickListener {
            isBookmarked = !isBookmarked
            if(isBookmarked){
                bookmarkButton.setImageResource(R.drawable.bookmarks_icon_colored)
            } else {
                bookmarkButton.setImageResource(R.drawable.bookmarks_icon)
            }
        }

        // Expand/Collapse button click event
        expandButton.setOnClickListener {
            if (content.maxLines == 5) {
                content.maxLines = Integer.MAX_VALUE  // 전체 텍스트 표시
                expandButton.text = "접기"  // 버튼 텍스트 변경
            } else {
                content.maxLines = 5  // 텍스트를 5줄까지만 표시
                expandButton.text = "자세히 보기"  // 버튼 텍스트 변경
            }

        }
    }

}