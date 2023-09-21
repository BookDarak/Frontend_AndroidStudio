package com.cookandroid.bookdarak_1

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cookandroid.bookdarak_1.data.model.FBook
import com.cookandroid.bookdarak_1.databinding.ActivityEditreviewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class editreview : AppCompatActivity() {

    val TAG: String = "editreviewActivity"

    private lateinit var binding: ActivityEditreviewBinding
    //private lateinit var db: FindBookDataBase

    private var model: FBook? = null
    private var userId: Int = -1
    private var bookId: Int = -1
    private var reviewId: Int = -1
    private var firstIsbn: String = "-1"
    var startdateString_edit=""
    var finishdateString_edit=""




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textEditreviewBooktitle.text = title



        binding.writingreviewBackButton.setOnClickListener {


            val intent = Intent(this@editreview, BookinfoActivity::class.java)
            intent.putExtra("bookModel", model)
            startActivity(intent)
        }



        userId = intent.getIntExtra("USER_ID", -1)
        bookId = intent.getIntExtra("BOOK_ID", -1)
        reviewId = intent.getIntExtra("REVIEW_ID", -1)
        firstIsbn = intent.getStringExtra("FIRST_ISBN").toString()

        Log.d(TAG, "editreview_userandbookandreviewid: $userId,$bookId,$reviewId,$firstIsbn")







        model = intent.getParcelableExtra("bookModel")

        binding.calendarButtonStartEdit.setOnClickListener {
            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                startdateString_edit = "${year}-${(month + 1).toString().padStart(2, '0')}-${dayOfMonth.toString().padStart(2, '0')}"
                binding.editStartday.text = startdateString_edit
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(
                Calendar.MONTH),cal.get(
                Calendar.DAY_OF_MONTH)).show()
        }

        binding.calendarButtonFinishEdit.setOnClickListener {
            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                finishdateString_edit = "${year}-${(month + 1).toString().padStart(2, '0')}-${dayOfMonth.toString().padStart(2, '0')}"
                binding.editFinishday.text = finishdateString_edit
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
                    Log.d(TAG, "editrveiew_results: $editreviewresults")



                    editreviewresults?.let {
                        val editablecontent = Editable.Factory.getInstance().newEditable(it.content)
                        val editablerating = Editable.Factory.getInstance().newEditable(it.rating.toString())
                        val editablephrase = Editable.Factory.getInstance().newEditable(it.phrase)
                        val editablepublicYn = Editable.Factory.getInstance().newEditable(it.publicYn)
                        val editablelikecount = Editable.Factory.getInstance().newEditable(it.likeCount.toString())
                        val editablestartdate = Editable.Factory.getInstance().newEditable(it.startDate)
                        val editableenddate = Editable.Factory.getInstance().newEditable(it.endDate)

                        binding.editreviewRatingbar.rating = it.rating
                        binding.editEditReview.text = editablecontent
                        binding.editEditImpressive.text = editablephrase
                        binding.editStartday.text = editablestartdate
                        binding.editFinishday.text = editableenddate

                        if(it.publicYn == "Y"){
                            binding.editRadioGroup.check(R.id.edit_rg_btn1_w)
                        }
                        else{
                            binding.editRadioGroup.check(R.id.edit_rg_btn2_w)
                        }

                    }



                    binding.editButtonRecord.setOnClickListener {

                        val title = binding.textEditreviewBooktitle.text.toString()
                        val isbn = binding.textEditreviewIsbn.text.toString()

                        val ratingString = binding.editreviewRatingbar.rating
                        val rating = String.format("%.1f", ratingString)//레이팅을 문자로 바꿈
                        val rating_2 = binding.editreviewRatingbar.rating

                        val content = binding.editEditReview.text.toString()
                        val phrase = binding.editEditImpressive.text.toString()
                        val selectedRadioButtonId = binding.editRadioGroup.checkedRadioButtonId

                        val publicYn = when (selectedRadioButtonId) {
                            R.id.edit_rg_btn1_w -> "Y"
                            R.id.edit_rg_btn2_w -> "N"
                            else -> ""
                        }

                        val startDate = startdateString_edit
                        val endDate = finishdateString_edit


                        val UpdateReviewRequest = UpdateReviewRequest(rating, content, phrase, publicYn, startDate, endDate) // gender 추가
                        Log.d(TAG, "reviewrequest: $rating, $content, $phrase, $publicYn, $startDate, $endDate")

                        ApiClient.service.updateReview(reviewId,UpdateReviewRequest).enqueue(object:
                            Callback<UpdateReviewResponse> {
                            override fun onResponse(call: Call<UpdateReviewResponse>, response: Response<UpdateReviewResponse>) {
                                if (response.isSuccessful && response.body()?.isSuccess == true) {
                                    val message = response.body()?.message

                                    Toast.makeText(this@editreview, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()

                                    val intent = Intent(this@editreview, seereview_Activity::class.java)
                                    intent.putExtra("REVIEW_ID", reviewId)
                                    intent.putExtra("USER_ID", userId)
                                    intent.putExtra("BOOK_ID", bookId)
                                    intent.putExtra("bookModel", model)
                                    intent.putExtra("rating_2",rating_2)
                                    intent.putExtra("publicYn",publicYn)

                                    intent.putExtra("firstisbn",firstIsbn)
                                    intent.putExtra("title",title)

                                    intent.putExtra("content",content)
                                    intent.putExtra("phrase",phrase)

                                    intent.putExtra("startdate",startDate)
                                    intent.putExtra("enddate",endDate)
                                    startActivity(intent)

                                    Log.d(TAG, "editreview_content: $content")










                                } else {
                                    Toast.makeText(this@editreview, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<UpdateReviewResponse>, t: Throwable) {
                                Toast.makeText(this@editreview, t.localizedMessage, Toast.LENGTH_SHORT).show()
                            }
                        })









                    }










                } else {
                    Toast.makeText(this@editreview, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ReviewDetailResponse>, t: Throwable) {
                Toast.makeText(this@editreview, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })

        renderView()



    }







    private fun renderView() {

        binding.textEditreviewBooktitle.text = model?.title.orEmpty()
        val isbnn = model?.isbn.toString().split(" ")
        val isbnnn = isbnn[0]
        binding.textEditreviewIsbn.text = isbnnn



        Glide.with(binding.imageEditreviewBookcover.context)
            .load(model?.thumbnail.orEmpty())
            .into(binding.imageEditreviewBookcover)




    }




}



