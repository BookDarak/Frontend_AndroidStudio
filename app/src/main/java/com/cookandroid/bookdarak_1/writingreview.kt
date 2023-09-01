package com.cookandroid.bookdarak_1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cookandroid.bookdarak_1.data.model.FBook
import com.cookandroid.bookdarak_1.databinding.ActivityWritingreviewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class writingreview : AppCompatActivity() {

    val TAG: String = "writingreviewActivity"

    private lateinit var binding: ActivityWritingreviewBinding
    //private lateinit var db: FindBookDataBase

    private var model: FBook? = null
    private var userId: Int = -1
    private var bookId: Int = -1
    private var reviewId: Int = -1





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWritingreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.writingreviewBackButton.setOnClickListener {


            val intent = Intent(this@writingreview, BookinfoActivity::class.java)
            intent.putExtra("bookModel", model)
            startActivity(intent)
        }



            userId = intent.getIntExtra("USER_ID", -1)
            bookId = intent.getIntExtra("BOOK_ID", -1)





            //db = getAppDatabase(this)

            model = intent.getParcelableExtra("bookModel")

            renderView()

            initSaveButton()

        }



    private fun initSaveButton() {
        binding.buttonRecord.setOnClickListener {

            val title = binding.textWritingreviewBooktitle.text.toString()
            val isbn = binding.textWritingreviewIsbn.text.toString()

            val ratingString = binding.writingreviewRatingbar.rating
            val rating = String.format("%.1f", ratingString)//레이팅을 문자로 바꿈
            val rating_2 = binding.writingreviewRatingbar.rating

            val content = binding.editReview.text.toString()
            val phrase = binding.editImpressive.text.toString()
            val selectedRadioButtonId = binding.radioGroup.checkedRadioButtonId

            val publicYn = when (selectedRadioButtonId) {
                R.id.rg_btn1_w -> "(공개)"
                R.id.rg_btn2_w -> "(비공개)"
                else -> ""
            }

            val startDate = binding.startday.text.toString()
            val endDate = binding.finishday.text.toString()


            val reviewrequest = ReviewRequest(rating, content, phrase, publicYn, startDate, endDate) // gender 추가


            ApiClient.service.writeReview(userId, bookId, reviewrequest).enqueue(object:
                Callback<ReviewResponse> {
                override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        val reviewId = response.body()?.result?.reviewId ?: -1
                        Log.d(TAG, "reviewID: $reviewId")
                        //Log.d(TAG, "userId: $userId, bookId: $bookId")



                        val intent = Intent(this@writingreview, ReviewFragment::class.java)
                        intent.putExtra("REVIEW_ID", reviewId)
                        startActivity(intent)


                    } else {
                        Toast.makeText(this@writingreview, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                    Toast.makeText(this@writingreview, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })

            /*

            ApiClient.service.getReviewId(userId, bookId).enqueue(object: Callback<ReviewIdResponse> {
                override fun onResponse(call: Call<ReviewIdResponse>, response: Response<ReviewIdResponse>) {
                    if (response.isSuccessful) {
                        val reviewId = response.body()?.result?.reviewId
                        //val intent = Intent(this@writingreview, ReviewFragment::class.java)
                        //intent.putExtra("REVIEW_ID", reviewId)


                        //startActivity(intent)


                    } else {
                        Toast.makeText(this@writingreview, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ReviewIdResponse>, t: Throwable) {
                    Toast.makeText(this@writingreview, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })

             */
/*
            val intent = Intent(this@writingreview, seereview_Activity::class.java)
            intent.putExtra("content",content)
            intent.putExtra("phrase",phrase)
            intent.putExtra("rating_2",rating_2)
            intent.putExtra("publicYn",publicYn)
            intent.putExtra("startdate",startDate)
            intent.putExtra("enddate",endDate)
            intent.putExtra("isbn",isbn)
            intent.putExtra("title",title)
            intent.putExtra("bookModel", model)



            startActivity(intent)

 */




        }
    }



    private fun renderView() {

        binding.textWritingreviewBooktitle.text = model?.title.orEmpty()
        binding.textWritingreviewIsbn.text = model?.isbn.orEmpty()


        Glide.with(binding.imageWritingreviewBookcover.context)
            .load(model?.thumbnail.orEmpty())
            .into(binding.imageWritingreviewBookcover)




    }




 }



