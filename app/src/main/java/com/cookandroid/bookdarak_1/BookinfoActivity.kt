package com.cookandroid.bookdarak_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_bookinfo.*

class BookinfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookinfo)

        writebutton.setOnClickListener({
            val intent = Intent(this, writingreview::class.java)
            startActivity(intent)
        })
    }
}