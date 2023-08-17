package adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.bookdarak_1.R
import model.Book

class BookAdapter(private val books: List<Book>) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_reviewbox, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        Log.d("BookAdapter", "Binding book at position $position: $book")
        holder.bind(book)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val bookImage: ImageView = itemView.findViewById(R.id.book_image_cal)
        private val bookTitle: TextView = itemView.findViewById(R.id.book_title_cal)
        private val bookAuthor: TextView = itemView.findViewById(R.id.book_author_cal)

        fun bind(book: Book) {
            bookImage.setImageResource(book.imageResource)
            bookTitle.text = book.title
            bookAuthor.text = book.author
        }
    }

}