package adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cookandroid.bookdarak_1.R
import com.cookandroid.bookdarak_1.ReviewSummaryResponse

class ReviewAdapter(private val reviews: List<ReviewSummaryResponse.ReviewSummaryItem>) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.review_item, parent, false)
        return ReviewViewHolder(view)
    }



    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        Log.d("ReviewAdapter", "Binding review at position $position: $review")
        holder.bind(review)
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    fun submitData(newData: List<ReviewSummaryResponse.ReviewSummaryItem>) {
        reviews.clear()
        reviews.addAll(newData)
        notifyDataSetChanged()
    }

    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val bookImage: ImageView = itemView.findViewById(R.id.book_image)
        private val content: TextView = itemView.findViewById(R.id.review_content)
        //private val createdDate: TextView = itemView.findViewById(R.id.createdDate)

        fun bind(review: ReviewSummaryResponse.ReviewSummaryItem) {
            Glide.with(itemView.context)
                .load(review.bookImgUrl)
                .into(bookImage)
            content.text = review.content
            //bookAuthor.text = review.author
        }
    }
}