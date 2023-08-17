package com.cookandroid.bookdarak_1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BookinfoActivity : AppCompatActivity() {
    private var bookmarked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookinfo)

        findViewById<ImageButton>(R.id.writebutton).setOnClickListener {
            val intent = Intent(this, writingreview::class.java)
            startActivity(intent)
        }

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
            if (isBookmarked) {
                bookmarkButton.setImageResource(R.drawable.bookmarks_icon_colored)
            } else {
                bookmarkButton.setImageResource(R.drawable.bookmarks_icon)
            }
        }

        // Expand/Collapse button click event
        expandButton.setOnClickListener {
            if (content.maxLines == 5) {
                content.maxLines = Integer.MAX_VALUE // Display the full text
                expandButton.text = "접기" // Change button text
            } else {
                content.maxLines = 5 // Display up to 5 lines of text
                expandButton.text = "자세히 보기" // Change button text
            }
        }
    }
}
