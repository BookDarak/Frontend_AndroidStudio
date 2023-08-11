package adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.cookandroid.bookdarak_1.R
import model.FindBookListDTO
import com.cookandroid.bookdarak_1.databinding.FindbookItemBinding

class BookFindAdapter(private val itemClickedListener: (FindBookListDTO)->Unit) : ListAdapter<FindBookListDTO, BookFindAdapter.BookItemViewHolder>(diffUtil) {

    // 뷰 바인딩 (item_book.xml)
    inner class BookItemViewHolder(private val binding: FindbookItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(bookModel: FindBookListDTO){
            binding.textBooktitle.text = bookModel.title
            binding.textBookdescription.text = bookModel.description

            binding.root.setOnClickListener {
                itemClickedListener(bookModel)
            }

            // Glide 사용 하기
            Glide
                .with(binding.imageBookcover.context)
                .load(bookModel.coverurl)
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
        val diffUtil = object: DiffUtil.ItemCallback<FindBookListDTO>(){
            override fun areItemsTheSame(oldItem: FindBookListDTO, newItem: FindBookListDTO): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FindBookListDTO, newItem: FindBookListDTO): Boolean {
                return oldItem.title == newItem.title
            }

        }
    }
}
