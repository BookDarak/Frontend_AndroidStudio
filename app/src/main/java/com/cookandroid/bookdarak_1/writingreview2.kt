package com.cookandroid.bookdarak_1

import android.app.DatePickerDialog
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
import java.util.*

class writingreview2 : AppCompatActivity() {

    val TAG: String = "writingreviewActivity"

    private lateinit var binding: ActivityWritingreview2Binding



    private var userId: Int = -1
    private var bookId: Int = -1
    private var frontisbn: String = "-1"
    private var bookinfo_home: FBook? = null
    private var reviewId: Int = -1
    var startdateString2=""
    var finishdateString2=""





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWritingreview2Binding.inflate(layoutInflater)
        setContentView(binding.root)






        userId = intent.getIntExtra("USER_ID", -1)
        bookId = intent.getIntExtra("BOOK_ID", -1)
        frontisbn = intent.getStringExtra("FRONT_ISBN").toString()

        val bookinfo_home = intent.getSerializableExtra("bookinfo_home") as? BookInfo_home
        Log.d(TAG, "writingreview2_intent: $userId,$bookId,$bookinfo_home,$frontisbn")




        binding.textWritingreviewBooktitle2.text = bookinfo_home?.title.orEmpty()
        binding.textWritingreviewIsbn2.text = frontisbn

        Glide.with(binding.imageWritingreviewBookcover2.context)
            .load(bookinfo_home?.thumbnail.orEmpty())
            .into(binding.imageWritingreviewBookcover2)


        val intent = Intent(this@writingreview2, seereview2::class.java)
        //intent.putExtra("REVIEW_ID", reviewId)

        intent.putExtra("bookinfo_home",bookinfo_home)

        binding.calendarButtonStart2.setOnClickListener {
            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                startdateString2 = "${year}-${(month + 1).toString().padStart(2, '0')}-${dayOfMonth.toString().padStart(2, '0')}"
                binding.startday2.text = startdateString2
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(
                Calendar.DAY_OF_MONTH)).show()
        }

        binding.calendarButtonFinish2.setOnClickListener {
            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                finishdateString2 = "${year}-${(month + 1).toString().padStart(2, '0')}-${dayOfMonth.toString().padStart(2, '0')}"
                binding.finishday2.text = finishdateString2
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(
                Calendar.DAY_OF_MONTH)).show()
        }


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



            val startDate = startdateString2
            val endDate = finishdateString2


            val reviewrequest = ReviewRequest(rating, content, phrase, publicYn, startDate, endDate) // gender 추가
            Log.d(TAG, "reviewrequest: $rating, $content, $phrase, $publicYn, $startDate, $endDate")

            ApiClient.service.writeReview(userId, bookId, reviewrequest).enqueue(object:
                Callback<ReviewResponse> {
                override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        val reviewId = response.body()?.result?.reviewId ?: -1
                        Log.d(TAG, "reviewID: $reviewId")


                        val intent = Intent(this@writingreview2, seereview2::class.java)
                        //intent.putExtra("REVIEW_ID", reviewId)
                        intent.putExtra("USER_ID", userId)
                        intent.putExtra("REVIEW_ID", reviewId)
                        intent.putExtra("BOOK_ID", bookId)
                        intent.putExtra("rating_2",rating_2)
                        intent.putExtra("publicYn",publicYn)
                        intent.putExtra("bookinfo_home",bookinfo_home)


                        intent.putExtra("FRONT_ISBN",frontisbn)
                        intent.putExtra("title",title)

                        intent.putExtra("content",content)
                        intent.putExtra("phrase",phrase)

                        intent.putExtra("startdate",startDate)
                        intent.putExtra("enddate",endDate)
                        startActivity(intent)







                    } else {
                        Toast.makeText(this@writingreview2, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                    Toast.makeText(this@writingreview2, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })







        }


    }




}

