package com.cookandroid.bookdarak_1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cookandroid.bookdarak_1.data.model.FBook
import com.cookandroid.bookdarak_1.databinding.ActivityWritingreviewBinding
import model.Find_Review

class writingreview : AppCompatActivity() {

    private lateinit var binding: ActivityWritingreviewBinding
    private lateinit var db: FindBookDataBase

    private var model: FBook? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWritingreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = getAppDatabase(this)

        model = intent.getParcelableExtra("bookModel")

        renderView()

        initSaveButton()
    }

    private fun initSaveButton() {
        binding.buttonRecord.setOnClickListener {
            Thread {
                db.reviewDao().saveReview(
                    Find_Review(
                        model?.isbn ?: "0",
                        binding.editReview.text.toString()
                    )
                )
            }.start()
            val intent = Intent(this, BookinfoActivity::class.java)
            intent.putExtra("bookModel", model)
            startActivity(intent)
        }
    }

    private fun renderView() {

        binding.textWritingreviewBooktitle.text = model?.title.orEmpty()
        //binding.textWritingreviewBooktitle.text = model?.title.orEmpty()


        Glide.with(binding.imageWritingreviewBookcover.context)
            .load(model?.thumbnail.orEmpty())
            .into(binding.imageWritingreviewBookcover)




    }


}