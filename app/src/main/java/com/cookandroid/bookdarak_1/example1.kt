package com.cookandroid.bookdarak_1

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class example1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example1)

        findViewById<TextView>(R.id.review_content).setOnClickListener {
            val intent = Intent(this, example2::class.java)
            startActivity(intent)
        }
    }
}