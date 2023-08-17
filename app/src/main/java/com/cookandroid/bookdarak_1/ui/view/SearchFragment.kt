package com.cookandroid.bookdarak_1.ui.view

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cookandroid.bookdarak_1.databinding.FragmentSearchBinding
import com.cookandroid.bookdarak_1.ui.adapter.BookSearchLoadStateAdapter
import com.cookandroid.bookdarak_1.ui.adapter.BookSearchPagingAdapter
import com.cookandroid.bookdarak_1.ui.viewmodel.SearchViewModel
import com.cookandroid.bookdarak_1.util.collectLatestStateFlow
import com.cookandroid.bookdarak_1.util.constants.SEARCH_BOOKS_TIME_DELAY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment: Fragment(){
    //뷰바인딩 설정,
    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!


    private val searchViewModel by viewModels<SearchViewModel>()
    private lateinit var bookSearchAdapter: BookSearchPagingAdapter

    //바인딩 초기화
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    //뷰모델 초기화
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        searchBooks()
        setupLoadState()


        collectLatestStateFlow(searchViewModel.searchPagingResult) {
            bookSearchAdapter.submitData(it)
        }
    }

    private fun setupRecyclerView(){
        //bookSearchAdapter = BookSearchAdapter()
        bookSearchAdapter = BookSearchPagingAdapter()
        binding.rvSearchResult.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            //아이템 사이에 줄 그어줌
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

            adapter = bookSearchAdapter.withLoadStateFooter(
                footer = BookSearchLoadStateAdapter(bookSearchAdapter::retry)
            )
        }

        //어댑터에 클릭리스너 달아주기
        bookSearchAdapter.setOnItemClickListener {
            // 북프래그먼트로 이동해라!
            val action = SearchFragmentDirections.actionFragmentSearchToFragmentBook(it)
            findNavController().navigate(action)
        }
    }

    private fun searchBooks(){
        //사용자의 입력 시간 간격 조절을 위해
        var startTime = System.currentTimeMillis()
        var endTime: Long

        //쿼리 불러오기
        binding.etSearch.text = Editable.Factory.getInstance().newEditable(searchViewModel.query)

        //알아서 갖고 오기
        binding.etSearch.addTextChangedListener { text: Editable? ->
            endTime = System.currentTimeMillis()
            if (endTime - startTime >= SEARCH_BOOKS_TIME_DELAY){
                text?.let {
                    //공백 자르기
                    val query = it.toString().trim()
                    if (query.isNotEmpty()){
                        //값 가져오기
                        //bookSearchViewModel.searchBooks(query)
                        //페이징으로 가져오기
                        searchViewModel.searchBooksPaging(query)
                        //쿼리 저장하기
                        searchViewModel.query = query
                    }
                }
            }
            startTime = endTime
        }
    }

    //네트워크 로딩 상태 핸들링을 위한 함수
    private fun setupLoadState() {
        bookSearchAdapter.addLoadStateListener { combinedLoadStates ->
            //이걸로 로딩 상태를 볼 수 있음
            val loadState = combinedLoadStates.source

            // 리스트가 비어있는지 확인
            val isListEmpty = bookSearchAdapter.itemCount < 1
                    && loadState.refresh is LoadState.NotLoading
                    && loadState.append.endOfPaginationReached

            // 리스트 상태에 따라 활성화 및 비활성화
            binding.tvEmptylist.isVisible = isListEmpty
            binding.rvSearchResult.isVisible = !isListEmpty

            binding.progressBar.isVisible = loadState.refresh is LoadState.Loading


        }


    }

    //바인딩 해제를 위함
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}