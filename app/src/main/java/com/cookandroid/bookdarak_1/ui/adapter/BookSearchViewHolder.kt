package com.cookandroid.bookdarak_1.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.cookandroid.bookdarak_1.data.model.FBook
import com.cookandroid.bookdarak_1.databinding.ItemBookPreviewBinding

class BookSearchViewHolder( private val binding: ItemBookPreviewBinding ) : RecyclerView.ViewHolder(binding.root) {

    fun bind(fbook: FBook){
        val author = fbook.authors.toString().removeSurrounding("[", "]")
        val publisher = fbook.publisher
        val date = if (fbook.datetime.isNotEmpty()) fbook.datetime.substring(0, 10) else ""

        itemView.apply {
            binding.ivArticleImage.load(fbook.thumbnail)
            binding.tvTitle.text = fbook.title
            binding.tvAuthor.text = "$author | $publisher"
            binding.tvDatetime.text = date
        }
    }
}