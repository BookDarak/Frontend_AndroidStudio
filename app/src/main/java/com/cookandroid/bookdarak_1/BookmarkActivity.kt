package com.cookandroid.bookdarak_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BookmarkActivity : AppCompatActivity() {

    // Define the data class inside BookmarkActivity
    private var userId: Int = -1

    class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bookImage: ImageView = view.findViewById(R.id.book_image)
        val bookTitle: TextView = view.findViewById(R.id.book_title)
    }

    class BookAdapter(private val books: List<model.Book>) : RecyclerView.Adapter<BookViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false)
            return BookViewHolder(view)
        }

        override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
            val book = books[position]

            // 이미지 로딩
            Glide.with(holder.itemView.context)
                .load(book.image) // URL을 사용하여 이미지 로딩
                .override(200, 350)
                .into(holder.bookImage)

            holder.bookTitle.text = book.title
        }


        override fun getItemCount() = books.size
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_bookmarklist)

        userId = intent.getIntExtra("USER_ID", -1)
        val userName = intent.getStringExtra("USER_NAME")

        val userNameTextView = findViewById<TextView>(R.id.user_name)
        userNameTextView.text = "$userName 님의 북마크"

        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }


        // Set up RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.bookmark_list)
        recyclerView.layoutManager = GridLayoutManager(this, 2) // 2 columns
        //recyclerView.adapter = BookAdapter(books)
        getBookmarks()

    }

    private fun getBookmarks() {
        ApiClient.service.getBookmarks(userId).enqueue(object : Callback<BookmarkListResponse> {
            override fun onResponse(
                call: Call<BookmarkListResponse>,
                response: Response<BookmarkListResponse>
            ) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    val bookmarks = response.body()?.result?.items
                    bookmarks?.let {
                        updateRecyclerView(it)
                    }
                } else {
                    // Handle the error response
                    // For example: Toast.makeText(this@BookmarkActivity, "Failed to fetch bookmarks", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BookmarkListResponse>, t: Throwable) {
                // Handle the failure scenario
                // For example: Toast.makeText(this@BookmarkActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateRecyclerView(books: List<BookItem>) {
        val recyclerView = findViewById<RecyclerView>(R.id.bookmark_list)
        recyclerView.adapter = BookAdapter(books.map {
            model.Book(
                image = it.bookImgUrl, // this is a placeholder, you might want to load images via URL with Glide
                title = it.name,
                author = "" // assuming you don't have author info in Bookmark API, otherwise you can fill it
            )
        })
    }



}
