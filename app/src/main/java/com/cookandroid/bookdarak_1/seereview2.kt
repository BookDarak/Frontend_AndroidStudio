package com.cookandroid.bookdarak_1

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cookandroid.bookdarak_1.data.model.BookInfo_home
import com.cookandroid.bookdarak_1.data.model.FBook
import com.cookandroid.bookdarak_1.databinding.ActivitySeereview2Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class seereview2 : AppCompatActivity() {
    private lateinit var binding: ActivitySeereview2Binding

    private var bookinfo_home: FBook? = null
    private var userId: Int = -1
    private var reviewId: Int = -1
    private var frontisbn: String = "-1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeereview2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        val bookinfo_home = intent.getSerializableExtra("bookinfo_home") as? BookInfo_home
        userId = intent.getIntExtra("USER_ID", -1)
        reviewId = intent.getIntExtra("REVIEW_ID", -1)
        frontisbn = intent.getStringExtra("FRONT_ISBN").toString()//바꾸기

        Log.d(ContentValues.TAG, "seereview2_intent: $userId,$reviewId,$bookinfo_home,$frontisbn")



        binding.textSeereviewBooktitle2.text = bookinfo_home?.title.orEmpty()
        binding.textSeereviewIsbn2.text = frontisbn

        Glide.with(binding.imageSeereviewBookcover2.context)
            .load(bookinfo_home?.thumbnail.orEmpty())
            .into(binding.imageSeereviewBookcover2)

        ApiClient.service.getReviewDetail(reviewId).enqueue(object:
            Callback<ReviewDetailResponse> {
            override fun onResponse(call: Call<ReviewDetailResponse>, response: Response<ReviewDetailResponse>) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    val seereview2results = response.body()?.result
                    Log.d(ContentValues.TAG, "seereview2_results: $seereview2results")



                    seereview2results?.let {
                        val editablecontent = Editable.Factory.getInstance().newEditable(it.content)
                        val editablerating = Editable.Factory.getInstance().newEditable(it.rating.toString())
                        val editablephrase = Editable.Factory.getInstance().newEditable(it.phrase)
                        val editablepublicYn = Editable.Factory.getInstance().newEditable(it.publicYn)
                        val editablelikecount = Editable.Factory.getInstance().newEditable(it.likeCount.toString())
                        val editablestartdate = Editable.Factory.getInstance().newEditable(it.startDate)
                        val editableenddate = Editable.Factory.getInstance().newEditable(it.endDate)

                        binding.seeRatingbar2.rating = it.rating
                        binding.textReview2.text = editablecontent
                        binding.textImpressive2.text = editablephrase
                        binding.seeStartday2.text = editablestartdate
                        binding.seeFinishday2.text = editableenddate


                        if (it.publicYn == "Y") {
                            getString(R.string.y)  // Replace with the appropriate string resource
                        } else {
                            getString(R.string.n) // Replace with the appropriate string resource
                        }
                        binding.textviewPublicOr2.text = it.publicYn

                    }




                } else {
                    Toast.makeText(this@seereview2, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ReviewDetailResponse>, t: Throwable) {
                Toast.makeText(this@seereview2, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })









        val button_ok2: Button = findViewById(R.id.button_ok2)
        button_ok2.setOnClickListener {

            val intent = Intent(this@seereview2, NaviActivity::class.java)

            intent.putExtra("USER_ID", userId)

            startActivity(intent)




        }


    }

}


