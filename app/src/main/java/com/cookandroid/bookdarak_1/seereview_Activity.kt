package com.cookandroid.bookdarak_1

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cookandroid.bookdarak_1.data.model.FBook
import com.cookandroid.bookdarak_1.databinding.ActivitySeereviewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class seereview_Activity : AppCompatActivity() {
    private lateinit var binding: ActivitySeereviewBinding

    private var model: FBook? = null
    private var userId: Int = -1
    private var bookId: Int = -1
    private var reviewId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeereviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        model = intent.getParcelableExtra("bookModel")
        userId = intent.getIntExtra("USER_ID", -1)
        bookId = intent.getIntExtra("BOOK_ID", -1)
        reviewId = intent.getIntExtra("REVIEW_ID", -1)
        Log.d(TAG, "seereview_userandreviewid: $userId,$reviewId,$bookId")




        ApiClient.service.getReviewDetail(reviewId).enqueue(object:
            Callback<ReviewDetailResponse> {
            override fun onResponse(call: Call<ReviewDetailResponse>, response: Response<ReviewDetailResponse>) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    val seereviewresults = response.body()?.result
                    Log.d(TAG, "seereview_results: $seereviewresults")



                    seereviewresults?.let {
                        val editablecontent = Editable.Factory.getInstance().newEditable(it.content)
                        val editablerating = Editable.Factory.getInstance().newEditable(it.rating.toString())
                        val editablephrase = Editable.Factory.getInstance().newEditable(it.phrase)
                        val editablepublicYn = Editable.Factory.getInstance().newEditable(it.publicYn)
                        val editablelikecount = Editable.Factory.getInstance().newEditable(it.likeCount.toString())
                        val editablestartdate = Editable.Factory.getInstance().newEditable(it.startDate)
                        val editableenddate = Editable.Factory.getInstance().newEditable(it.endDate)

                        binding.seeRatingbar.rating = it.rating
                        binding.textReview.text = editablecontent
                        binding.textImpressive.text = editablephrase
                        binding.seeStartday.text = editablestartdate
                        binding.seeFinishday.text = editableenddate


                        if (it.publicYn == "Y") {
                            getString(R.string.y)  // Replace with the appropriate string resource
                        } else {
                            getString(R.string.n) // Replace with the appropriate string resource
                        }
                        binding.textviewPublicOr.text = it.publicYn

                    }




                } else {
                    Toast.makeText(this@seereview_Activity, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ReviewDetailResponse>, t: Throwable) {
                Toast.makeText(this@seereview_Activity, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })

        binding.buttonDelete.setOnClickListener {



            ApiClient.service.deleteReview(reviewId).enqueue(object:
                Callback<DeleteReviewResponse> {
                override fun onResponse(call: Call<DeleteReviewResponse>, response: Response<DeleteReviewResponse>) {
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        val result = response.body()?.result
                        Log.d(TAG, "seereview_deleteresult: $result")


                        val intent = Intent(this@seereview_Activity, NaviActivity::class.java)

                        intent.putExtra("USER_ID", userId)

                        startActivity(intent)



                    } else {
                        Toast.makeText(this@seereview_Activity, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DeleteReviewResponse>, t: Throwable) {
                    Toast.makeText(this@seereview_Activity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })







        }

        binding.buttonModify.setOnClickListener {



            val intent = Intent(this@seereview_Activity, editreview::class.java)

            intent.putExtra("USER_ID", userId)
            intent.putExtra("REVIEW_ID", reviewId)
            intent.putExtra("BOOK_ID", bookId)
            intent.putExtra("bookModel", model)

            startActivity(intent)







        }





        renderView()


        val button_ok: Button = findViewById(R.id.button_ok)
        button_ok.setOnClickListener {

            val intent = Intent(this@seereview_Activity, NaviActivity::class.java)

            intent.putExtra("USER_ID", userId)

            startActivity(intent)







        }








    }
    private fun renderView() {

        binding.textSeereviewBooktitle.text = model?.title.orEmpty()
        val isbnn = model?.isbn.toString().split(" ")
        val isbnnn = isbnn[0]
        binding.textSeereviewIsbn.text = isbnnn


        Glide.with(binding.imageSeereviewBookcover.context)
            .load(model?.thumbnail.orEmpty())
            .into(binding.imageSeereviewBookcover)




    }
}

