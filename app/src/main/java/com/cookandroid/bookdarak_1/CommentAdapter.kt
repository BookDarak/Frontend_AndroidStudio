package com.cookandroid.bookdarak_1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommentAdapter(private val context: Context) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    private val commentItems = mutableListOf<CommentItem>()

    fun setCommentItems(items: List<CommentItem>) {
        commentItems.clear()
        commentItems.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val commentItem = commentItems[position]
        holder.bind(commentItem)
    }

    override fun getItemCount(): Int = commentItems.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val commentText: TextView = view.findViewById(R.id.commentText)

        fun bind(commentItem: CommentItem) {
            commentText.text = commentItem.content
            // TODO: Bind other views if needed
        }
    }
}
