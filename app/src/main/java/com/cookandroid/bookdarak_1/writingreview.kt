package com.cookandroid.bookdarak_1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cookandroid.bookdarak_1.data.model.FBook
import com.cookandroid.bookdarak_1.databinding.ActivityWritingreviewBinding

class writingreview : AppCompatActivity() {

    private lateinit var binding: ActivityWritingreviewBinding
    private lateinit var db: FindBookDataBase

    private var model: FBook? = null
    private var userId: Int = -1
    private var bookId: Int = -1


    companion object {
        fun newInstance(context: Context, userId: Int, bookId: Int): Intent {
            val intent = Intent(context, writingreview::class.java)
            val args = Bundle()
            args.putInt("USER_ID", userId)
            args.putInt("BOOK_ID", bookId)
            intent.putExtras(args)
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWritingreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findViewById<Button>(R.id.button_record).setOnClickListener {
            val intent = Intent(this, example2::class.java)
            startActivity(intent)
        }

            findViewById<ImageButton>(R.id.writingreview_back_button).setOnClickListener {
                val intent = Intent(this, BookinfoActivity::class.java)
                intent.putExtra("bookModel", model)
                startActivity(intent)
            }


            userId = intent.getIntExtra("USER_ID", -1)
            bookId = intent.getIntExtra("BOOK_ID", -1)





            db = getAppDatabase(this)

            model = intent.getParcelableExtra("bookModel")

            renderView()

           // initSaveButton()

        }


/*
    private fun initSaveButton() {
        binding.buttonRecord.setOnClickListener {
            val ratingString = binding.writingreviewRatingbar.rating
            val rating = String.format("%.1f", ratingString)//레이팅을 문자로 바꿈

            val content = binding.editReview.text.toString()
            val phrase = binding.editImpressive.text.toString()
            val selectedRadioButtonId = binding.radioGroup.checkedRadioButtonId

            val publicYn = when (selectedRadioButtonId) {
                R.id.rg_btn1 -> "public"
                R.id.rg_btn2 -> "non-public"
                else -> ""
            }

            val startDate = binding.startday.text.toString()
            val endDate = binding.finishday.text.toString()


            val reviewrequest = ReviewRequest(rating, content, phrase, publicYn, startDate, endDate) // gender 추가


            ApiClient.service.writeReview(userId, bookId, reviewrequest).enqueue(object: Callback<ReviewResponse> {
                override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        val reviewId = response.body()?.result?.reviewId ?: -1
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

            val intent = Intent(this@writingreview, seereview_Activity::class.java)
            intent.putExtra("content",content)
            intent.putExtra("phrase",phrase)
            intent.putExtra("rating",rating)
            //intent.putExtra("publicYn",publicYn)
            intent.putExtra("startdate",startDate)
            intent.putExtra("enddate",endDate)



            startActivity(intent)




        }
    }
*/


    private fun renderView() {

        binding.textWritingreviewBooktitle.text = model?.title.orEmpty()
        binding.textWritingreviewIsbn.text = model?.isbn.orEmpty()


        Glide.with(binding.imageWritingreviewBookcover.context)
            .load(model?.thumbnail.orEmpty())
            .into(binding.imageWritingreviewBookcover)




    }




 }



