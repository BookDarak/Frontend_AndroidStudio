package com.cookandroid.bookdarak_1.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.bookdarak_1.databinding.FragmentFavoritBinding
import com.cookandroid.bookdarak_1.ui.adapter.BookSearchPagingAdapter
import com.cookandroid.bookdarak_1.ui.viewmodel.FavoriteViewModel
import com.cookandroid.bookdarak_1.util.collectLatestStateFlow
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment: Fragment(){
    //뷰바인딩 설정,
    private var _binding : FragmentFavoritBinding? = null
    private val binding get() = _binding!!

    //뷰모델
    //힐트 안쓴 버전
    //private lateinit var bookSearchViewModel: BookSearchViewModel
    //힐트 쓴 버전
    //private val bookSearchViewModel by activityViewModels<BookSearchViewModel>()
    private val favoriteViewModel by viewModels<FavoriteViewModel>()

    //어댑터
    //private lateinit var bookSearchAdapter: BookSearchAdapter
    //페이징 어댑터로 교환!
    private lateinit var bookSearchAdapter: BookSearchPagingAdapter

    //바인딩 초기화
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavoritBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //뷰모델 ㅍ초기화
        //힐트 사용 필요 ㄴ
        //bookSearchViewModel = (activity as MainActivity).bookSearchViewModel

        //리사이클러뷰 초기화
        setupRecyclerView()
        setupTouchHelper(view)

        /*
        //좋아요 책 옵저빙
        bookSearchViewModel.favoriteBooks.observe(viewLifecycleOwner){
            bookSearchAdapter.submitList(it)
        }
        */

        //Flow를 이용
        /*
        lifecycleScope.launch {
            bookSearchViewModel.favoriteBooks.collectLatest {
                bookSearchAdapter.submitList(it)
            }
        }
        */

        //StateFlow를 이용
        /*
        collectLatestStateFlow(bookSearchViewModel.favoriteBooks){
            bookSearchAdapter.submitList(it)
        }
        */

        //Paging을 이용
        collectLatestStateFlow(favoriteViewModel.favoritePagingBooks) {
            bookSearchAdapter.submitData(it)
        }
    }

    //리사이클러 뷰 초기화
    private fun setupRecyclerView(){
        //bookSearchAdapter = BookSearchAdapter()
        bookSearchAdapter = BookSearchPagingAdapter()

        binding.rvFavoriteBooks.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            //아이템 사이에 줄 그어줌
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            adapter = bookSearchAdapter
        }

        //어댑터에 클릭리스너 달아주기
        bookSearchAdapter.setOnItemClickListener {
            // 북프래그먼트로 이동해라!
            val action = FavoriteFragmentDirections.actionFragmentFavoriteToFragmentBook(it)
            findNavController().navigate(action)
        }
    }

    //움직였을 때 기능 정의
    private fun setupTouchHelper(view: View){

        //콜백 정의 후
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //페이징 안쓸 때
                /*
                val position = viewHolder.bindingAdapterPosition
                val book = bookSearchAdapter.currentList[position]
                bookSearchViewModel.deleteBook(book)
                Snackbar.make(view, "Book has deleted", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo"){
                        bookSearchViewModel.saveBook(book)
                    }
                }.show()
                */

                //페이징 쓸 때
                val position = viewHolder.bindingAdapterPosition
                val pagedBook = bookSearchAdapter.peek(position)
                pagedBook?.let { fBook ->
                    favoriteViewModel.deleteBook(fBook)
                    Snackbar.make(view, "Book has deleted", Snackbar.LENGTH_SHORT).apply {
                        setAction("Undo") {
                            favoriteViewModel.saveBook(fBook)
                        }
                    }.show()
                }
            }
        }

        //등록하기
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvFavoriteBooks)
        }
    }
    //바인딩 해제를 위함
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}