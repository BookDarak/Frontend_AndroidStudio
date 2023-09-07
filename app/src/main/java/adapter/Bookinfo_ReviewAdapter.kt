package adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.bookdarak_1.R
import com.cookandroid.bookdarak_1.ReviewListResponse

class Bookinfo_ReviewAdapter(private val reviewlists: MutableList<ReviewListResponse.ReviewItem>) : RecyclerView.Adapter<Bookinfo_ReviewAdapter.Bookinfo_ReviewViewHolder>() {


    class Bookinfo_ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val bookImage: ImageView = itemView.findViewById(R.id.book_image)
        private val bookId: TextView = itemView.findViewById(R.id.bookid)
        private val userId: TextView = itemView.findViewById(R.id.userid)
        private val reviewId: TextView = itemView.findViewById(R.id.reviewid)
        private val content: TextView = itemView.findViewById(R.id.review_content)
        private val username: TextView = itemView.findViewById(R.id.review_item_username)
        private val rating: RatingBar = itemView.findViewById(R.id.review_item_rating)
        private val createdDate: TextView = itemView.findViewById(R.id.createdDate)
        private val likeCount: TextView = itemView.findViewById(R.id.likecount)

        fun bind(reviewlist: ReviewListResponse.ReviewItem) {

            content.text = reviewlist.content
            username.text = reviewlist.username
            rating.rating = reviewlist.rating.toFloat()
            createdDate.text = reviewlist.createdDate
            likeCount.text = reviewlist.likeCount.toString()
            userId.text = reviewlist.userId.toString()
            bookId.text = reviewlist.bookId.toString()
            reviewId.text = reviewlist.reviewId.toString()
            //bookAuthor.text = review.author
        }


    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Bookinfo_ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.certain_bookreview_item, parent, false)
        return Bookinfo_ReviewViewHolder(view)

    }


    override fun onBindViewHolder(holder: Bookinfo_ReviewViewHolder, position: Int) {
        val reviewlist = reviewlists[position]

        Log.d("Bookinfo_ReviewAdapter", "Binding review at position $position: $reviewlist")
        holder.bind(reviewlist)


    }

    fun submitData(newData: List<ReviewListResponse.ReviewItem>) {
        reviewlists.clear()
        reviewlists.addAll(newData)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return reviewlists.size
    }





}