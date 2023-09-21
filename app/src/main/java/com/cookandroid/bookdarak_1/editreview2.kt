package com.cookandroid.bookdarak_1

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cookandroid.bookdarak_1.data.model.BookInfo_home
import com.cookandroid.bookdarak_1.databinding.ActivityEditreview2Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class editreview2 : AppCompatActivity() {

    val TAG: String = "editreview2Activity"

    private lateinit var binding: ActivityEditreview2Binding
    //private lateinit var db: FindBookDataBase

    private var bookinfo_home: BookInfo_home? = null
    private var userId: Int = -1
    private var bookId: Int = -1
    private var reviewId: Int = -1
    private var frontisbn = ""
    var startdateString_edit2=""
    var finishdateString_edit2=""





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditreview2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textEditreviewBooktitle2.text = title







        userId = intent.getIntExtra("USER_ID", -1)
        bookId = intent.getIntExtra("BOOK_ID", -1)
        reviewId = intent.getIntExtra("REVIEW_ID", -1)
        bookinfo_home = intent.getSerializableExtra("bookinfo_home") as? BookInfo_home
        frontisbn = intent.getStringExtra("FRONT_ISBN").toString()
        Log.d(TAG, "editreview2_userandbookandreviewid: $userId,$bookId,$reviewId,$bookinfo_home,$frontisbn")





        binding.calendarButtonStart2Edit.setOnClickListener {
            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                startdateString_edit2 = "${year}-${(month + 1).toString().padStart(2, '0')}-${dayOfMonth.toString().padStart(2, '0')}"
                binding.edit2Startday.text = startdateString_edit2
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(
                Calendar.MONTH),cal.get(
                Calendar.DAY_OF_MONTH)).show()
        }

        binding.calendarButtonFinish2Edit.setOnClickListener {
            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                finishdateString_edit2 = "${year}-${(month + 1).toString().padStart(2, '0')}-${dayOfMonth.toString().padStart(2, '0')}"
                binding.edit2Finishday.text = finishdateString_edit2
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(
                Calendar.MONTH),cal.get(
                Calendar.DAY_OF_MONTH)).show()
        }




        ApiClient.service.getReviewDetail(reviewId).enqueue(object:
            Callback<ReviewDetailResponse> {
            override fun onResponse(call: Call<ReviewDetailResponse>, response: Response<ReviewDetailResponse>) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    val editreviewresults = response.body()?.result
                    Log.d(TAG, "editreiew_results: $editreviewresults")



                    editreviewresults?.let {
                        val editablecontent = Editable.Factory.getInstance().newEditable(it.content)
                        val editablerating = Editable.Factory.getInstance().newEditable(it.rating.toString())
                        val editablephrase = Editable.Factory.getInstance().newEditable(it.phrase)
                        val editablepublicYn = Editable.Factory.getInstance().newEditable(it.publicYn)
                        val editablelikecount = Editable.Factory.getInstance().newEditable(it.likeCount.toString())
                        val editablestartdate = Editable.Factory.getInstance().newEditable(it.startDate)
                        val editableenddate = Editable.Factory.getInstance().newEditable(it.endDate)

                        binding.editreview2Ratingbar.rating = it.rating
                        binding.editEditReview2.text = editablecontent
                        binding.editEditImpressive2.text = editablephrase
                        binding.edit2Startday.text = editablestartdate
                        binding.edit2Finishday.text = editableenddate

                        if(it.publicYn == "Y"){
                            binding.edit2RadioGroup.check(R.id.edit2_rg_btn1_w)
                        }
                        else{
                            binding.edit2RadioGroup.check(R.id.edit2_rg_btn2_w)
                        }

                    }

                    binding.editButtonRecord2.setOnClickListener {

                        val title = binding.textEditreviewBooktitle2.text.toString()
                        val isbn = binding.textEditreviewIsbn2.text.toString()

                        val ratingString = binding.editreview2Ratingbar.rating
                        val rating = String.format("%.1f", ratingString)//레이팅을 문자로 바꿈
                        val rating_2 = binding.editreview2Ratingbar.rating

                        val content = binding.editEditReview2.text.toString()
                        val phrase = binding.editEditImpressive2.text.toString()
                        val selectedRadioButtonId = binding.edit2RadioGroup.checkedRadioButtonId

                        val publicYn = when (selectedRadioButtonId) {
                            R.id.edit2_rg_btn1_w -> "Y"
                            R.id.edit2_rg_btn2_w -> "N"
                            else -> ""
                        }

                        val startDate = startdateString_edit2
                        val endDate = finishdateString_edit2



                        val UpdateReviewRequest = UpdateReviewRequest(rating, content, phrase, publicYn, startDate, endDate) // gender 추가
                        Log.d(TAG, "reviewrequest: $rating, $content, $phrase, $publicYn, $startDate, $endDate")

                        ApiClient.service.updateReview(reviewId,UpdateReviewRequest).enqueue(object:
                            Callback<UpdateReviewResponse> {
                            override fun onResponse(call: Call<UpdateReviewResponse>, response: Response<UpdateReviewResponse>) {
                                if (response.isSuccessful && response.body()?.isSuccess == true) {
                                    val message = response.body()?.message

                                    Toast.makeText(this@editreview2, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()

                                    val intent = Intent(this@editreview2, seereview2::class.java)
                                    intent.putExtra("REVIEW_ID", reviewId)
                                    intent.putExtra("USER_ID", userId)
                                    intent.putExtra("BOOK_ID", bookId)
                                    intent.putExtra("bookinfo_home", bookinfo_home)
                                    intent.putExtra("rating_2",rating_2)
                                    intent.putExtra("publicYn",publicYn)

                                    intent.putExtra("FRONT_ISBN",frontisbn)
                                    intent.putExtra("title",title)

                                    intent.putExtra("content",content)
                                    intent.putExtra("phrase",phrase)

                                    intent.putExtra("startdate",startDate)
                                    intent.putExtra("enddate",endDate)
                                    startActivity(intent)

                                    Log.d(TAG, "editreview2_content: $content")










                                } else {
                                    Toast.makeText(this@editreview2, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<UpdateReviewResponse>, t: Throwable) {
                                Toast.makeText(this@editreview2, t.localizedMessage, Toast.LENGTH_SHORT).show()
                            }
                        })









                    }










                } else {
                    Toast.makeText(this@editreview2, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ReviewDetailResponse>, t: Throwable) {
                Toast.makeText(this@editreview2, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })

        renderView()



    }







    private fun renderView() {

        binding.textEditreviewBooktitle2.text = bookinfo_home?.title.orEmpty()
        val isbnn =  bookinfo_home?.isbn.toString().split(" ")
        val isbnnn = isbnn[0]
        binding.textEditreviewIsbn2.text = isbnnn

        Glide.with(binding.imageEditreviewBookcover2.context)
            .load(bookinfo_home?.thumbnail.orEmpty())
            .into(binding.imageEditreviewBookcover2)




    }




}



