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

class BookmarkActivity : AppCompatActivity() {

    // Define the data class inside BookmarkActivity
    data class Book(val image: Int, val title: String)

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
            //holder.bookImage.setImageResource(book.image)
            holder.bookTitle.text = book.title
        }

        override fun getItemCount() = books.size
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_bookmarklist)

        // Create sample data
        val books = listOf(
            Book(R.drawable.book_sample, "책 제목 1"),
            Book(R.drawable.book_sample, "책 제목 2"),
            // ...
        )

        // Set up RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.bookmark_list)
        recyclerView.layoutManager = GridLayoutManager(this, 2) // 2 columns
        //recyclerView.adapter = BookAdapter(books)
    }
}
