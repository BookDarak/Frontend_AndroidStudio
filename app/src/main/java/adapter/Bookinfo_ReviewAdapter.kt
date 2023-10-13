package adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.bookdarak_1.R
import com.cookandroid.bookdarak_1.ReviewListResponse

class Bookinfo_ReviewAdapter(private val reviewlists: MutableList<ReviewListResponse.ReviewItem>) : RecyclerView.Adapter<Bookinfo_ReviewAdapter.Bookinfo_ReviewViewHolder>() {

    /*
    private var onThumbsUpClickListener: OnThumbsUpClickListener? = null
    private var onUsernameClickListener: OnUsernameClickListener? = null


    fun setOnThumbsUpClickListener(listener: OnThumbsUpClickListener?) {
        onThumbsUpClickListener = listener
    }


    fun setOnUsernameClickListener(listener: OnUsernameClickListener?) {
        onUsernameClickListener = listener
    }

     */

    class Bookinfo_ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //private val bookImage: ImageView = itemView.findViewById(R.id.book_image)

        private val content: TextView = itemView.findViewById(R.id.certain_review_content)
        private val username: TextView = itemView.findViewById(R.id.certain_username)
        private val rating: RatingBar = itemView.findViewById(R.id.certain_item_rating)
        private val createdDate: TextView = itemView.findViewById(R.id.certain_review_createdDate)
        private val likeCount: TextView = itemView.findViewById(R.id.certain_likecount)



        fun bind(reviewlist: ReviewListResponse.ReviewItem) {

            content.text = reviewlist.content
            username.text = reviewlist.username
            rating.rating = reviewlist.rating.toFloat()
            createdDate.text = reviewlist.createdDate
            likeCount.text = reviewlist.likeCount.toString()

            //bookAuthor.text = review.author
        }
/*
        thumbsUpButton.setOnClickListener {
            onThumbsUpClickListener?.onThumbsUpClick(reviewlists.reviewId)
        }

        bookImage.setOnClickListener {
            onBookImageClickListener?.onBookImageClick(review.bookId)
        }
        username.setOnClickListener {
            onUsernameClickListener?.onUsernameClick(review.userId)
        }


 */

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