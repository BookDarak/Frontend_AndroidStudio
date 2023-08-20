package com.cookandroid.bookdarak_1.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cookandroid.bookdarak_1.data.model.FBook
import com.cookandroid.bookdarak_1.databinding.FindbookItemBinding

class BookSearchViewHolder(private val itemClickedListener: (FBook)->Unit) : ListAdapter<FBook, BookSearchViewHolder.BookItemViewHolder>(diffUtil) {

    // 뷰 바인딩 (item_book.xml)
    inner class BookItemViewHolder(private val binding: FindbookItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(bookModel: FBook){
            binding.textBooktitle.text = bookModel.title
            binding.textBookcontents.text = bookModel.contents
            binding.textBookauthor.text = bookModel.authors.toString()

            binding.root.setOnClickListener {
                itemClickedListener(bookModel)
            }

            // Glide 사용 하기
            Glide
                .with(binding.imageBookcover.context)
                .load(bookModel.thumbnail)
                .into(binding.imageBookcover)
        }
    }

    // 미리 만들어진 뷰 홀더가 없을 경우 생성하는 함수.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemViewHolder {
        return BookItemViewHolder(FindbookItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    // 실제 뷰 홀더가 뷰에 그려지게 됬을 때 데이터를 바인드하게 되는 함수.
    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object{
        // 같은 값이 있으면 할당 해줄 필요 없다
        val diffUtil = object: DiffUtil.ItemCallback<FBook>(){
            override fun areItemsTheSame(oldItem: FBook, newItem: FBook): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FBook, newItem: FBook): Boolean {
                return oldItem.isbn == newItem.isbn
            }

        }
    }
}