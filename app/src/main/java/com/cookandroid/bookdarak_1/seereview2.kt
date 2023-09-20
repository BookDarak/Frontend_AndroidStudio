package com.cookandroid.bookdarak_1

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cookandroid.bookdarak_1.data.model.BookInfo_home
import com.cookandroid.bookdarak_1.data.model.FBook
import com.cookandroid.bookdarak_1.databinding.ActivitySeereview2Binding


class seereview2 : AppCompatActivity() {
    private lateinit var binding: ActivitySeereview2Binding

    private var bookinfo_home: FBook? = null
    private var userId: Int = -1
    //private var reviewId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeereview2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        val bookinfo_home = intent.getSerializableExtra("bookinfo_home") as? BookInfo_home
        userId = intent.getIntExtra("USER_ID", -1)
        //reviewId = intent.getIntExtra("REVIEW_ID", -1)
        Log.d(ContentValues.TAG, "seereview2_intent: $userId,$bookinfo_home")



        binding.textSeereviewBooktitle2.text = bookinfo_home?.title.orEmpty()
        binding.textSeereviewIsbn2.text = bookinfo_home?.isbn.orEmpty()

        Glide.with(binding.imageSeereviewBookcover2.context)
            .load(bookinfo_home?.thumbnail.orEmpty())
            .into(binding.imageSeereviewBookcover2)

        //Get content and phrase from intent extras
        val content = intent.getStringExtra("content")
        val phrase = intent.getStringExtra("phrase")
        val rating_2 = intent.getFloatExtra("rating_2", 0.0f)
        val startDate = intent.getStringExtra("startdate")
        val endDate = intent.getStringExtra("enddate")
        val publicYnString = intent.getStringExtra("publicYn")
        val title = intent.getStringExtra("title")
        val isbn = intent.getStringExtra("isbn")

        // Use the content and phrase data to update your views
        binding.textReview2.text = content
        binding.textImpressive2.text = phrase
        binding.seeRatingbar2.rating = rating_2
        binding.seeStartday2.text = startDate
        binding.seeFinishday2.text = endDate
        binding.textSeereviewBooktitle2.text = title
        binding.textSeereviewIsbn2.text = isbn


        //Handle the publicYn value and update appropriate view
        if (publicYnString == "public") {
            getString(R.string.y)  // Replace with the appropriate string resource
        } else {
            getString(R.string.n) // Replace with the appropriate string resource
        }
        binding.textviewPublicOr2.text = publicYnString






        val button_ok2: Button = findViewById(R.id.button_ok2)
        button_ok2.setOnClickListener {

            val intent = Intent(this@seereview2, NaviActivity::class.java)

            intent.putExtra("USER_ID", userId)

            startActivity(intent)




        }


    }

}


