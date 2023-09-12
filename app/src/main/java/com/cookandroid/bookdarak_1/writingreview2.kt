package com.cookandroid.bookdarak_1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cookandroid.bookdarak_1.data.model.BookInfo_home
import com.cookandroid.bookdarak_1.data.model.FBook
import com.cookandroid.bookdarak_1.databinding.ActivityWritingreview2Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class writingreview2 : AppCompatActivity() {

    val TAG: String = "writingreviewActivity"

    private lateinit var binding: ActivityWritingreview2Binding



    private var userId: Int = -1
    private var bookId: Int = -1
    private var bookinfo_home: FBook? = null
    private var reviewId: Int = -1





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWritingreview2Binding.inflate(layoutInflater)
        setContentView(binding.root)






        userId = intent.getIntExtra("USER_ID", -1)
        bookId = intent.getIntExtra("BOOK_ID", -1)

        val bookinfo_home = intent.getSerializableExtra("bookinfo_home") as? BookInfo_home
        Log.d(TAG, "writingreview2_intent: $userId,$bookId,$bookinfo_home")




        binding.textWritingreviewBooktitle2.text = bookinfo_home?.title.orEmpty()
        binding.textWritingreviewIsbn2.text = bookinfo_home?.isbn.orEmpty()

        Glide.with(binding.imageWritingreviewBookcover2.context)
            .load(bookinfo_home?.thumbnail.orEmpty())
            .into(binding.imageWritingreviewBookcover2)


        val intent = Intent(this@writingreview2, seereview2::class.java)
        //intent.putExtra("REVIEW_ID", reviewId)

        intent.putExtra("bookinfo_home",bookinfo_home)


        binding.buttonRecord2.setOnClickListener {

            val title = binding.textWritingreviewBooktitle2.text.toString()
            val isbn = binding.textWritingreviewIsbn2.text.toString()

            val ratingString = binding.writingreviewRatingbar2.rating
            val rating = String.format("%.1f", ratingString)//레이팅을 문자로 바꿈
            val rating_2 = binding.writingreviewRatingbar2.rating

            val content = binding.editReview2.text.toString()
            val phrase = binding.editImpressive2.text.toString()
            val selectedRadioButtonId = binding.radioGroup2.checkedRadioButtonId

            val publicYn = when (selectedRadioButtonId) {
                R.id.rg_btn1_w2 -> "Y"
                R.id.rg_btn2_w2 -> "N"
                else -> ""
            }

            val startDate = binding.startday2.text.toString()
            val endDate = binding.finishday2.text.toString()


            val reviewrequest = ReviewRequest(rating, content, phrase, publicYn, startDate, endDate) // gender 추가
            Log.d(TAG, "reviewrequest: $rating, $content, $phrase, $publicYn, $startDate, $endDate")

            ApiClient.service.writeReview(userId, bookId, reviewrequest).enqueue(object:
                Callback<ReviewResponse> {
                override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        val reviewId = response.body()?.result?.reviewId ?: -1
                        Log.d(TAG, "reviewID: $reviewId")
                        //Log.d(TAG, "userId: $userId, bookId: $bookId")







                    } else {
                        Toast.makeText(this@writingreview2, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                    Toast.makeText(this@writingreview2, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })

            val intent = Intent(this@writingreview2, seereview2::class.java)
            //intent.putExtra("REVIEW_ID", reviewId)
            intent.putExtra("USER_ID", userId)
            intent.putExtra("rating_2",rating_2)
            intent.putExtra("publicYn",publicYn)
            intent.putExtra("bookinfo_home",bookinfo_home)


            intent.putExtra("isbn",isbn)
            intent.putExtra("title",title)

            intent.putExtra("content",content)
            intent.putExtra("phrase",phrase)

            intent.putExtra("startdate",startDate)
            intent.putExtra("enddate",endDate)
            startActivity(intent)





        }


    }




}

