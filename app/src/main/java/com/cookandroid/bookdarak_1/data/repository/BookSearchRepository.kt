package com.cookandroid.bookdarak_1.data.repository

import androidx.paging.PagingData
import com.cookandroid.bookdarak_1.data.model.FBook
import com.cookandroid.bookdarak_1.data.model.SearchResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface BookSearchRepository {

    suspend fun searchBooks(
        query: String,
        sort: String,
        page: Int,
        size: Int,
    ): Response<SearchResponse>

    // Room
    suspend fun insertBooks(book: FBook)

    suspend fun deleteBooks(book: FBook)

    fun getFavoriteBooks(): Flow<List<FBook>>

    // DataStore
    suspend fun saveSortMode(mode: String)

    suspend fun getSortMode(): Flow<String>

    suspend fun saveCacheDeleteMode(mode: Boolean)

    suspend fun getCacheDeleteMode(): Flow<Boolean>

    // Paging
    fun getFavoritePagingBooks(): Flow<PagingData<FBook>>

    fun searchBooksPaging(query: String, sort: String): Flow<PagingData<FBook>>
}