package com.cookandroid.bookdarak_1

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cookandroid.bookdarak_1.data.model.FBook
import com.cookandroid.bookdarak_1.databinding.ActivityWritingreviewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class writingreview : AppCompatActivity() {

    val TAG: String = "writingreviewActivity"

    private lateinit var binding: ActivityWritingreviewBinding
    //private lateinit var db: FindBookDataBase

    private var model: FBook? = null
    private var userId: Int = -1
    private var bookId: Int = -1
    private var reviewId: Int = -1
    var startdateString=""
    var finishdateString=""





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWritingreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textWritingreviewBooktitle.text = title




        binding.writingreviewBackButton.setOnClickListener {

            reviewId = -1

            val intent = Intent(this@writingreview, BookinfoActivity::class.java)
            intent.putExtra("bookModel", model)
            intent.putExtra("REVIEW_Id", reviewId)
            intent.putExtra("USER_Id", userId)
            intent.putExtra("REVIEW_EDITED", reviewId != -1)
            startActivity(intent)
        }



            userId = intent.getIntExtra("USER_ID", -1)
            bookId = intent.getIntExtra("BOOK_ID", -1)
        Log.d(TAG, "writingreview_userandbookid: $userId,$bookId")






            //db = getAppDatabase(this)

            model = intent.getParcelableExtra("bookModel")

            renderView()

            initSaveButton()

        }







    private fun initSaveButton() {

        binding.calendarButtonStart.setOnClickListener {
            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                startdateString = "${year}-${(month + 1).toString().padStart(2, '0')}-${dayOfMonth.toString().padStart(2, '0')}"
                binding.startday.text = startdateString
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(
                Calendar.DAY_OF_MONTH)).show()
        }

        binding.calendarButtonFinish.setOnClickListener {
            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                finishdateString = "${year}-${(month + 1).toString().padStart(2, '0')}-${dayOfMonth.toString().padStart(2, '0')}"
                binding.finishday.text = finishdateString
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
        }

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
                R.id.rg_btn1_w -> "Y"
                R.id.rg_btn2_w -> "N"
                else -> ""
            }

            val startDate = startdateString
            val endDate = finishdateString




            val reviewrequest = ReviewRequest(rating, content, phrase, publicYn, startDate, endDate) // gender 추가
            Log.d(TAG, "reviewrequest: $rating, $content, $phrase, $publicYn, $startDate, $endDate")

            ApiClient.service.writeReview(userId, bookId, reviewrequest).enqueue(object:
                Callback<ReviewResponse> {
                override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        val reviewId = response.body()?.result?.reviewId ?: -1
                        Log.d(TAG, "reviewID: $reviewId")


                        val intent = Intent(this@writingreview, seereview_Activity::class.java)

                        intent.putExtra("USER_ID", userId)
                        intent.putExtra("BOOK_ID", bookId)
                        intent.putExtra("REVIEW_ID", reviewId)
                        intent.putExtra("bookModel", model)
                        intent.putExtra("rating_2",rating_2)
                        intent.putExtra("publicYn",publicYn)

                        intent.putExtra("isbn",isbn)
                        intent.putExtra("title",title)

                        intent.putExtra("content",content)
                        intent.putExtra("phrase",phrase)

                        intent.putExtra("startdate",startDate)
                        intent.putExtra("enddate",endDate)
                        startActivity(intent)









                    } else {
                        when(response.body()?.code) {
                            2040 -> dialog("평점을 입력해주세요.")
                            2041 -> dialog("서평 내용을 입력해주세요.")
                            2042 -> dialog("공개여부 형식이 올바르지 않습니다.")
                            2043 -> dialog("시작 독서일을 선택해주세요.")
                            2044 -> dialog("완료 독서일을 선택해주세요.")
                            2045 -> dialog("시작일은 완료일보다 앞서야 합니다.")
                            3040 -> dialog("해당 책의 서평이 이미 존재합니다.")

                            //else -> dialog("fail")
                        }
                    }
                }

                override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                    Toast.makeText(this@writingreview, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })







        }
    }



    private fun renderView() {

        binding.textWritingreviewBooktitle.text = model?.title.orEmpty()
        val isbnn = model?.isbn.toString().split(" ")
        val isbnnn = isbnn[0]
        binding.textWritingreviewIsbn.text = isbnnn


        Glide.with(binding.imageWritingreviewBookcover.context)
            .load(model?.thumbnail.orEmpty())
            .into(binding.imageWritingreviewBookcover)




    }

    fun dialog(type: String){
        var dialog = AlertDialog.Builder(this)

        when(type) {

            "평점을 입력해주세요." -> {
                dialog.setTitle("서평 작성 실패")
                dialog.setMessage("평점을 입력해주세요.")
            }
            "서평 내용을 입력해주세요." -> {
                dialog.setTitle("서평 작성 실패")
                dialog.setMessage("서평 내용을 입력해주세요.")
            }
            "공개여부 형식이 올바르지 않습니다." -> {
                dialog.setTitle("서평 작성 실패")
                dialog.setMessage("공개여부 형식이 올바르지 않습니다.")
            }
            "시작 독서일을 선택해주세요." -> {
                dialog.setTitle("서평 작성 실패")
                dialog.setMessage("시작 독서일을 선택해주세요.")
            }
            "완료 독서일을 선택해주세요." -> {
                dialog.setTitle("서평 작성 실패")
                dialog.setMessage("완료 독서일을 선택해주세요.")
            }
            "시작일은 완료일보다 앞서야 합니다." -> {
                dialog.setTitle("서평 작성 실패")
                dialog.setMessage("시작일은 완료일보다 앞서야 합니다.")
            }
            "해당 책의 서평이 이미 존재합니다." -> {
                dialog.setTitle("서평 작성 실패")
                dialog.setMessage("해당 책의 서평이 이미 존재합니다.")
            }
        }

        dialog.setPositiveButton("확인") { _, _ ->
            Log.d(TAG, "User acknowledged the dialog")
        }
        dialog.show()
    }




 }



