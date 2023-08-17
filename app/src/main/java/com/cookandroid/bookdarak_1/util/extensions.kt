package com.cookandroid.bookdarak_1.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.cookandroid.bookdarak_1.ui.view.FavoriteFragment
import com.cookandroid.bookdarak_1.ui.view.SearchFragment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun <T> FavoriteFragment.collectLatestStateFlow(flow: Flow<T>, collect: suspend (T) -> Unit){
    viewLifecycleOwner.lifecycleScope.launch{
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
            flow.collectLatest(collect)
        }
    }
}


fun <T> SearchFragment.collectLatestStateFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}