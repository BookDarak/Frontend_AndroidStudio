package com.cookandroid.bookdarak_1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class seereview_Activity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seereview)



        findViewById<ImageButton>(R.id.see_backbutton).setOnClickListener {
            val intent = Intent(this, BookinfoActivity::class.java)
            startActivity(intent)
        }

        // Get content and phrase from intent extras
        val content = intent.getStringExtra("content")
        val phrase = intent.getStringExtra("phrase")
        //val rating = intent.getFloatExtra("rating",0.0f)
        //val publicYn = intent.getStringExtra("publicYn")
        val startDate = intent.getStringExtra("startdate")
        val endDate = intent.getStringExtra("enddate")


        // Use the content and phrase data to update your views
        val textReview = findViewById<TextView>(R.id.text_review)
        val textImpressive = findViewById<TextView>(R.id.text_impressive)
        //val seeRatingBar= findViewById<RatingBar>(R.id.see_ratingbar)
        //val publicYn = findViewById<TextView>(R.id.see)
        val textStartDate= findViewById<TextView>(R.id.see_startday)
        val textEndDate = findViewById<TextView>(R.id.see_finishday)

        textReview.text = content
        textImpressive.text = phrase
        //seeRatingBar.rating = rating
        textStartDate.text = startDate
        textEndDate.text = endDate

        findViewById<Button>(R.id.button_bookinfo).setOnClickListener {
            val intent = Intent(this, example1::class.java)
            startActivity(intent)
        }



    }





}


