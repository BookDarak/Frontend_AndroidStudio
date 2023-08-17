package com.cookandroid.bookdarak_1

import androidx.appcompat.app.AppCompatActivity

class writingreview : AppCompatActivity() {

    /*private lateinit var binding: ActivityWritingreviewBinding

    private lateinit var db: FindBook_datamigration

    private var model: FindBookListDTO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWritingreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = getAppDatabase(this)

        model = intent.getParcelableExtra("bookModel")

        renderView()

        initSaveButton()
    }*/

    /*private fun initSaveButton() {
        binding.buttonRecord.setOnClickListener {
            Thread {
                db.reviewDao().saveReview(
                    FindBook_reviewmodel(
                        model?.toString()?: 0,
                        binding.editReview.text.toString()
                    )
                )
            }.start()
        }
    }*/

    /*private fun renderView() {

        binding.textWritingreviewBooktitle.text = model?.title.orEmpty()


        Glide.with(binding.imageWritingreviewBookcover.context)
            .load(model?.thumbnail.orEmpty())
            .into(binding.imageWritingreviewBookcover)


        // 저장된 리뷰 데이터 가져오기;
        Thread {
            val review = db.reviewDao().getOneReview(model?.title?.toString() ?: title)//findbooklistdto에 id있어야오류해결
            review?.let {
                runOnUiThread {
                    binding.editReview.setText(it.review)
                }
            }
        }.start()
    }*/
}

