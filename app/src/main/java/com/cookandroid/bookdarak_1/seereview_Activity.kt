package com.cookandroid.bookdarak_1

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cookandroid.bookdarak_1.data.model.FBook
import com.cookandroid.bookdarak_1.databinding.ActivitySeereviewBinding


class seereview_Activity : AppCompatActivity() {
    private lateinit var binding: ActivitySeereviewBinding

    private var model: FBook? = null
    private var userId: Int = -1
    //private var reviewId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeereviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        model = intent.getParcelableExtra("bookModel")
        userId = intent.getIntExtra("USER_ID", -1)
        //reviewId = intent.getIntExtra("REVIEW_ID", -1)
        Log.d(TAG, "seereview_userandreviewid: $userId")


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
                binding.textReview.text = content
                binding.textImpressive.text = phrase
               binding.seeRatingbar.rating = rating_2
               binding.seeStartday.text = startDate
               binding.seeFinishday.text = endDate
                binding.textSeereviewBooktitle.text = title
                binding.textSeereviewIsbn.text = isbn

                 //Handle the publicYn value and update appropriate view
                if (publicYnString == "public") {
                    getString(R.string.y)  // Replace with the appropriate string resource
                } else {
                    getString(R.string.n) // Replace with the appropriate string resource
                }
                binding.textviewPublicOr.text = publicYnString


/*
            ApiClient.service.getReviewDetail(reviewId).enqueue(object: Callback<ReviewDetailResponse> {
                override fun onResponse(call: Call<ReviewDetailResponse>, response: Response<ReviewDetailResponse>) {
                    if (response.isSuccessful) {
                        val rating = response.body()?.result?.rating ?: -1
                        val content = response.body()?.result?.content ?: -1
                        val phrase = response.body()?.result?.phrase ?: -1
                        val publicYn = response.body()?.result?.publicYn ?: -1
                        val likeCount = response.body()?.result?.likeCount ?: -1
                        val startDate = response.body()?.result?.startDate ?: -1
                        val endDate = response.body()?.result?.endDate ?: -1


                        //binding.textReview.text = content.toString()
                        //binding.textImpressive.text = phrase.toString()
                        //binding.seeRatingbar.rating = rating as Float
                        //binding.seeStartday.text = startDate.toString()
                        //binding.seeFinishday.text = endDate.toString()
                        //binding.textviewPublicOr.text = startDate.toString()
                        //binding.seeFinishday.text = endDate.toString()
                        //val reviewId = response.body()?.result?.reviewId
                        //val intent = Intent(this@writingreview, ReviewFragment::class.java)
                        //intent.putExtra("REVIEW_ID", reviewId)


                        //startActivity(intent)


                    } else {
                        Toast.makeText(this@seereview_Activity, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ReviewDetailResponse>, t: Throwable) {
                    Toast.makeText(this@seereview_Activity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })


 */



        renderView()


        val button_ok: Button = findViewById(R.id.button_ok)
        button_ok.setOnClickListener {

            val intent = Intent(this@seereview_Activity, NaviActivity::class.java)
            //intent.putExtra("bookModel", model)
            intent.putExtra("USER_ID", userId) // Passing userId to writingreview activity
            //intent.putExtra("BOOK_ID", bookId)
            //Log.d(ContentValues.TAG, "BookinfoActivity_user and bookID: $userId, $bookId")
            startActivity(intent)







        }








}
    private fun renderView() {

        //binding.textSeereviewBooktitle.text = model?.title.orEmpty()
        //binding.textSeereviewIsbn.text = model?.isbn.orEmpty()


        Glide.with(binding.imageSeereviewBookcover.context)
            .load(model?.thumbnail.orEmpty())
            .into(binding.imageSeereviewBookcover)




    }
}


