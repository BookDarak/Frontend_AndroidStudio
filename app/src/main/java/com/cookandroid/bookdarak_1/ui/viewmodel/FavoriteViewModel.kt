package com.cookandroid.bookdarak_1.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cookandroid.bookdarak_1.data.model.FBook
import com.cookandroid.bookdarak_1.data.repository.BookSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val bookSearchRepository: BookSearchRepository,
) : ViewModel() {

    // Paging
    val favoritePagingBooks: StateFlow<PagingData<FBook>> =
        bookSearchRepository.getFavoritePagingBooks()
            .cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PagingData.empty())

    // Room
    fun saveBook(fbook: FBook) = viewModelScope.launch {
        bookSearchRepository.insertBooks(fbook)
    }

    fun deleteBook(fbook: FBook) = viewModelScope.launch {
        bookSearchRepository.deleteBooks(fbook)
    }
}