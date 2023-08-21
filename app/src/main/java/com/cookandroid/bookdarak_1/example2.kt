package com.cookandroid.bookdarak_1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class example2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example2)

        findViewById<Button>(R.id.button_bookinfo).setOnClickListener {
            val intent = Intent(this, example1::class.java)
            startActivity(intent)
        }
    }
}