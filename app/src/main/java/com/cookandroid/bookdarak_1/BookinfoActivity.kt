package com.cookandroid.bookdarak_1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cookandroid.bookdarak_1.data.model.FBook
import com.cookandroid.bookdarak_1.databinding.ActivityBookinfoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookinfoActivity : AppCompatActivity() {
    private var bookmarked = false
    private lateinit var binding: ActivityBookinfoBinding

    //private lateinit var db: FindBookDataBase

    private var model: FBook? = null
    private var userId: Int = -1
    private var bookId: Int = -1











    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //db = getAppDatabase(this)
        binding = ActivityBookinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getIntExtra("USER_ID", -1)
        //bookId = intent.getIntExtra("BOOK_ID", -1)

        // Assuming your binding object is properly set up
        val authorsList = binding.writer.text // This assumes the binding syntax for retrieving a TextView's text

// Convert the list of authors to a readable string
        //val authorString = authorsList.joinToString(", ") // Join authors with a comma and space

// Now you can use the authorString as needed, such as displaying it in a TextView




        //binding.bookname.text = model?.title.orEmpty()
        //binding.writer.text = model?.authors.toString()
        //binding.textIsbn.text = model?.isbn.toString()


        //val writerText = model?.authors.toString()

// Now, isbnList contains the individual ISBN numbers as a list of strings



        val booktitle = model?.title.toString()
        //val writerList = writerText
        //    .trim('[', ']') // 괄호 제거
        //    .split("\" \"") // 작가 이름들을 분할하여 리스트로 변환
        val writerList = model?.authors
        val isbn = model?.isbn.toString()


        val image = model?.thumbnail.toString()

        val BookIdrequest =
            writerList?.let { BookIdRequest(booktitle, it, isbn, image) } // gender 추가


        if (BookIdrequest != null) {
            ApiClient.service.bookId(BookIdrequest).enqueue(object: Callback<BookIdResponse> {
                override fun onResponse(call: Call<BookIdResponse>, response: Response<BookIdResponse>) {
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        //val bookId = response.body()?.result?.bookId ?: -1
                        val intent = Intent(this@BookinfoActivity, writingreview::class.java)
                        intent.putExtra("BOOK_ID", bookId)





                        startActivity(intent)




                    } else {
                        Toast.makeText(this@BookinfoActivity, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<BookIdResponse>, t: Throwable) {
                    Toast.makeText(this@BookinfoActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })
        }




        model = intent.getParcelableExtra("bookModel")

        renderView()

        findViewById<Button>(R.id.writebutton).setOnClickListener {
            val intent = Intent(this, writingreview::class.java)
            intent.putExtra("bookModel", model)
            intent.putExtra("USER_ID", userId) // Passing userId to writingreview activity
            intent.putExtra("BOOK_ID", bookId)
            startActivity(intent)
        }

        val backButton = findViewById<ImageButton>(R.id.backButton)
        val bookmarkButton = findViewById<ImageButton>(R.id.bookmarkButton)
        val bookcontent = findViewById<TextView>(R.id.bookcontent)
        val expandButton = findViewById<Button>(R.id.expandButton)





        // Back button click event
        backButton.setOnClickListener {
            finish()
        }

        // Bookmark button click event
        var isBookmarked = false
        bookmarkButton.setOnClickListener {
            isBookmarked = !isBookmarked
            if(isBookmarked){
                bookmarkButton.setImageResource(R.drawable.bookmarks_icon_colored)
                ApiClient.service.addBookmark(userId, bookId).enqueue(object : Callback<BookmarkResponse> {
                    override fun onResponse(call: Call<BookmarkResponse>, response: Response<BookmarkResponse>) {
                        if (response.isSuccessful) {
                            if (response.body()?.isSuccess == true) {
                                // 북마크 추가 성공
                                Toast.makeText(this@BookinfoActivity,"북마크 목록에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                                val message = response.body()?.message
                                // message 등을 활용하여 사용자에게 알림 또는 작업 수행
                            } else {
                                // 북마크 추가 실패
                                Toast.makeText(this@BookinfoActivity,"북마크 목록 추가가 실패하였습니다.", Toast.LENGTH_SHORT).show()
                                val errorMessage = response.body()?.message
                                // errorMessage 등을 활용하여 사용자에게 알림 또는 작업 수행
                            }
                        } else {
                            // 서버 응답이 실패한 경우 처리
                        }
                    }

                    override fun onFailure(call: Call<BookmarkResponse>, t: Throwable) {
                        // 네트워크 오류 등 처리
                    }
                })


            } else {


                bookmarkButton.setImageResource(R.drawable.bookmarks_icon)
            }
        }

        // Expand/Collapse button click event
        expandButton.setOnClickListener {
            if (bookcontent.maxLines == 5) {
                bookcontent.maxLines = Integer.MAX_VALUE  // 전체 텍스트 표시
                expandButton.text = "접기"  // 버튼 텍스트 변경
            } else {
                bookcontent.maxLines = 5  // 텍스트를 5줄까지만 표시
                expandButton.text = "자세히 보기"  // 버튼 텍스트 변경
            }

        }
    }

    private fun renderView() {

        binding.bookname.text = model?.title.orEmpty()
        binding.writer.text = model?.authors.toString()
        binding.publisher.text = model?.publisher.orEmpty()
        binding.publishdate.text = model?.datetime.orEmpty()
        binding.price.text = model?.price.toString()
        binding.textIsbn.text = model?.isbn.toString()
        binding.bookcontent.text = model?.contents.toString()




        Glide.with(binding.imageView.context)
            .load(model?.thumbnail.orEmpty())
            .into(binding.imageView)

        //앱 구동할떄 이부분 주석처리해주세요!!!! 여기때문에 책 검색에서 책 누르면 화면 꺼집니다. 질문예정이구요!!
        // 저장된 리뷰 데이터 가져오기;
        /*

        Thread {
            val Find_Review = db.reviewDao().getOneReview(model?.isbn ?: "0")
            Find_Review?.let {
                runOnUiThread {
                    binding.savedReview.setText(it.Find_Review)
                }
            }
        }.start()

         */


    }




    }





