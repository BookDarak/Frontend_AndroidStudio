package com.cookandroid.bookdarak_1.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.cookandroid.bookdarak_1.data.model.FBook
import com.cookandroid.bookdarak_1.databinding.ItemBookPreviewBinding

class BookSearchPagingAdapter : PagingDataAdapter<FBook, BookSearchViewHolder>(BookDiffCallback) {

    override fun onBindViewHolder(holder: BookSearchViewHolder, position: Int) {
        val pagedBook = getItem(position)
        pagedBook?.let { fbook ->
            holder.bind(fbook)
            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it(fbook) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookSearchViewHolder {
        return BookSearchViewHolder(
            ItemBookPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    private var onItemClickListener: ((FBook) -> Unit)? = null
    fun setOnItemClickListener(listener: (FBook) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val BookDiffCallback = object : DiffUtil.ItemCallback<FBook>() {
            override fun areItemsTheSame(oldItem: FBook, newItem: FBook): Boolean {
                return oldItem.isbn == newItem.isbn
            }

            override fun areContentsTheSame(oldItem: FBook, newItem: FBook): Boolean {
                return oldItem == newItem
            }
        }
    }
}