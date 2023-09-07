package adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cookandroid.bookdarak_1.R
import com.cookandroid.bookdarak_1.ReviewSummaryResponse


private val ReviewAdapter.ReviewViewHolder.setOnThumbsUpClickListener: Unit
    get() {}

class ReviewAdapter(private val reviews: MutableList<ReviewSummaryResponse.ReviewSummaryItem>) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {




    private var onThumbsUpClickListener: OnThumbsUpClickListener? = null

    fun setOnThumbsUpClickListener(listener: OnThumbsUpClickListener?) {
        onThumbsUpClickListener = listener
    }



    interface OnThumbsUpClickListener {
        fun onThumbsUpClick(reviewId: Int)
    }








    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {





        private val bookImage: ImageView = itemView.findViewById(R.id.book_image)
        private val bookId: TextView = itemView.findViewById(R.id.book_id)
        private val userId: TextView = itemView.findViewById(R.id.user_id)
        private val content: TextView = itemView.findViewById(R.id.review_content)
        private val username: TextView = itemView.findViewById(R.id.review_item_username)
        private val rating: RatingBar = itemView.findViewById(R.id.review_item_rating)
        private val createdDate: TextView = itemView.findViewById(R.id.createdDate)
        private val likeCount: TextView = itemView.findViewById(R.id.likecount)
        private val thumbsUpButton: ImageButton = itemView.findViewById(R.id.thumbsup)



        init{
            thumbsUpButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onThumbsUpClickListener?.onThumbsUpClick(reviews[position].reviewId)
                }
            }
        }


        fun bind(review: ReviewSummaryResponse.ReviewSummaryItem) {
            Glide.with(itemView.context)
                .load(review.bookImgUrl)
                .into(bookImage)
            content.text = review.content
            username.text = review.username
            rating.rating = review.rating.toFloat()
            createdDate.text = review.createdDate
            likeCount.text = review.likeCount.toString()
            userId.text = review.userId.toString()
            bookId.text = review.bookId.toString()
            //bookAuthor.text = review.author





        }





    }








    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.review_item, parent, false)
        return ReviewViewHolder(view)

    }


    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]

        Log.d("ReviewAdapter", "Binding review at position $position: $review")
        holder.bind(review)
        holder.setOnThumbsUpClickListener




    }














    fun submitData(newData: List<ReviewSummaryResponse.ReviewSummaryItem>) {
        reviews.clear()
        reviews.addAll(newData)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return reviews.size
    }







}


