package adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import com.cookandroid.bookdarak_1.R
import model.BookFind

class BookFindAdapter(private val books: List<BookFind>) : RecyclerView.Adapter<BookFindAdapter.BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.findbook_item, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        Log.d("BookFindAdapter", "Binding book at position $position: $book")
        holder.bind(book)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val image_cover: ImageView = itemView.findViewById(R.id.image_bookcover)
        private val text_title: TextView = itemView.findViewById(R.id.text_booktitle)
        private val text_author: TextView = itemView.findViewById(R.id.text_bookauthor)
        private val text_description: TextView = itemView.findViewById(R.id.text_bookdescription)

        fun bind(book: BookFind) {
            image_cover.setImageResource(book.imageResource)
            text_title.text = book.title
            text_author.text = book.author
            text_description.text = book.description
        }
    }

}

