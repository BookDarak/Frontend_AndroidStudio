package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.cookandroid.bookdarak_1.R
import com.cookandroid.bookdarak_1.ReviewSummaryResponse



class ReviewAdapter(private val reviews: MutableList<ReviewSummaryResponse.ReviewSummaryItem>) : BaseAdapter() {
    private var onThumbsUpClickListener: OnThumbsUpClickListener? = null
    private var onBookImageClickListener: OnBookImageClickListener? = null

    fun setOnThumbsUpClickListener(listener: OnThumbsUpClickListener?) {
        onThumbsUpClickListener = listener
    }

    fun setOnBookImageClickListener(listener: OnBookImageClickListener?) {
        onBookImageClickListener = listener
    }

    override fun getCount(): Int {
        return reviews.size
    }

    override fun getItem(position: Int): Any {
        return reviews[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(parent?.context).inflate(R.layout.review_item, parent, false)
        val review = reviews[position]

        // Initialize your view elements here using 'view.findViewById' just like in your RecyclerView ViewHolder
        val bookImage: ImageButton = view.findViewById(R.id.book_image)
        val content: TextView = view.findViewById(R.id.review_content)
        val username: TextView = view.findViewById(R.id.review_item_username)
        val rating: RatingBar = view.findViewById(R.id.review_item_rating)
        val createdDate: TextView = view.findViewById(R.id.createdDate)
        val likeCount: TextView = view.findViewById(R.id.likecount)
        val thumbsUpButton: ImageButton = view.findViewById(R.id.thumbsup)

        // Set click listener for thumbs-up button
        thumbsUpButton.setOnClickListener {
            onThumbsUpClickListener?.onThumbsUpClick(review.reviewId)
        }

        bookImage.setOnClickListener {
            onBookImageClickListener?.onBookImageClick(review.bookId)
        }



        // Bind data to the view elements
        Glide.with(view.context)
            .load(review.bookImgUrl)
            .into(bookImage)
        content.text = review.content
        username.text = review.username
        rating.rating = review.rating.toFloat()
        createdDate.text = review.createdDate
        likeCount.text = review.likeCount.toString()

        return view
    }

    interface OnThumbsUpClickListener {
        fun onThumbsUpClick(reviewId: Int)

    }

    interface OnBookImageClickListener {
        fun onBookImageClick(bookId: Int)
    }

    // Method to update the adapter data
    fun submitData(newData: List<ReviewSummaryResponse.ReviewSummaryItem>) {
        reviews.clear()
        reviews.addAll(newData)
        notifyDataSetChanged() // Notify the adapter of data change
    }
}
