package adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.bookdarak_1.FindFragment
import com.cookandroid.bookdarak_1.databinding.FindbookHistoryItemBinding
import model.FindBook_historymodel


class BookFind_historyAdapter(val historyDeleteClickListener: (String) -> Unit, val mainActivity: FindFragment) :
        ListAdapter<FindBook_historymodel, BookFind_historyAdapter.HistoryViewHolder>(diffUtil) {



        inner class HistoryViewHolder(private val binding: FindbookHistoryItemBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(historyModel: FindBook_historymodel) {
                binding.textFindBookSearchhistory.text = historyModel.keyword
                binding.buttonFindBookSearchhistoryDelet.setOnClickListener {
                    historyDeleteClickListener(historyModel.keyword.orEmpty())
                }

                binding.root.setOnClickListener {
                    mainActivity.bookServiceSearchBook(historyModel.keyword.toString())
                }
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
            return HistoryViewHolder(
                FindbookHistoryItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
            holder.bind(currentList[position])
        }

        companion object {
            val diffUtil = object : DiffUtil.ItemCallback<FindBook_historymodel>() {
                override fun areContentsTheSame(oldItem: FindBook_historymodel, newItem: FindBook_historymodel) =
                    oldItem == newItem

                override fun areItemsTheSame(oldItem: FindBook_historymodel, newItem: FindBook_historymodel) =
                    oldItem.keyword == newItem.keyword
            }
        }

    }
