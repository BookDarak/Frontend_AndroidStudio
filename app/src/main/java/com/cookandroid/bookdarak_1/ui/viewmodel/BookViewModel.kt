package com.cookandroid.bookdarak_1.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cookandroid.bookdarak_1.data.model.FBook
import com.cookandroid.bookdarak_1.data.repository.BookSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val bookSearchRepository: BookSearchRepository,
) : ViewModel() {

    // Room
    fun saveBook(fbook: FBook) = viewModelScope.launch(Dispatchers.IO) {
        bookSearchRepository.insertBooks(fbook)
    }


    // For test
    val favoriteBooks: Flow<List<FBook>> = bookSearchRepository.getFavoriteBooks()
}