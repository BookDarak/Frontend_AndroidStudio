package com.cookandroid.bookdarak_1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cookandroid.bookdarak_1.databinding.ActivitySeereviewBinding


class seereview_Activity : AppCompatActivity() {
    private lateinit var binding: ActivitySeereviewBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeereviewBinding.inflate(layoutInflater)
        setContentView(binding.root)


                // Get content and phrase from intent extras
                val content = intent.getStringExtra("content")
                val phrase = intent.getStringExtra("phrase")
                val rating_2 = intent.getFloatExtra("rating_2", 0.0f)
                val startDate = intent.getStringExtra("startdate")
                val endDate = intent.getStringExtra("enddate")
                val publicYnString = intent.getStringExtra("publicYn")

                // Use the content and phrase data to update your views
                binding.textReview.text = content
                binding.textImpressive.text = phrase
                binding.seeRatingbar.rating = rating_2
                binding.seeStartday.text = startDate
                binding.seeFinishday.text = endDate

                 //Handle the publicYn value and update appropriate view
                if (publicYnString == "public") {
                    getString(R.string.y)  // Replace with the appropriate string resource
                } else {
                    getString(R.string.n) // Replace with the appropriate string resource
                }
                binding.textviewPublicOr.text = publicYnString












    }





}


