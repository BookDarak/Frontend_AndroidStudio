package com.cookandroid.bookdarak_1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cookandroid.bookdarak_1.data.model.FBook
import com.cookandroid.bookdarak_1.databinding.ActivityBookinfoBinding

class BookinfoActivity : AppCompatActivity() {
    private var bookmarked = false
    private lateinit var binding: ActivityBookinfoBinding

    private lateinit var db: FindBookDataBase

    private var model: FBook? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        db = getAppDatabase(this)

        model = intent.getParcelableExtra("bookModel")

        renderView()

        findViewById<Button>(R.id.writebutton).setOnClickListener {
            val intent = Intent(this, writingreview::class.java)
            intent.putExtra("bookModel", model)
            startActivity(intent)
        }

        val backButton = findViewById<ImageButton>(R.id.backButton)
        val bookmarkButton = findViewById<ImageButton>(R.id.bookmarkButton)
        val content = findViewById<TextView>(R.id.content)
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
            } else {
                bookmarkButton.setImageResource(R.drawable.bookmarks_icon)
            }
        }

        // Expand/Collapse button click event
        expandButton.setOnClickListener {
            if (content.maxLines == 5) {
                content.maxLines = Integer.MAX_VALUE  // 전체 텍스트 표시
                expandButton.text = "접기"  // 버튼 텍스트 변경
            } else {
                content.maxLines = 5  // 텍스트를 5줄까지만 표시
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


        binding.content.text = model?.contents.orEmpty()

        Glide.with(binding.imageView.context)
            .load(model?.thumbnail.orEmpty())
            .into(binding.imageView)

        //앱 구동할떄 이부분 주석처리해주세요!!!! 여기때문에 책 검색에서 책 누르면 화면 꺼집니다. 질문예정이구요!!
        // 저장된 리뷰 데이터 가져오기;
        Thread {
          val Find_Review = db.reviewDao().getOneReview(model?.isbn?:"0")
           Find_Review?.let {
               runOnUiThread {
                    binding.savedReview.setText(it.Find_Review)
                }
            }
        }.start()
        }
    }





