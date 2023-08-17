package com.cookandroid.bookdarak_1.data.repository
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cookandroid.bookdarak_1.data.api.FindBookAPI
import com.cookandroid.bookdarak_1.data.model.FBook
import com.cookandroid.bookdarak_1.util.constants.PAGING_SIZE
import retrofit2.HttpException
import java.io.IOException

class BookSearchPagingSource(
    private val api: FindBookAPI,
    private val query: String,
    private val sort: String,
) : PagingSource<Int, FBook>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FBook> {
        return try {
            val pageNumber = params.key ?: STARTING_PAGE_INDEX

            val response = api.FindBook(query, sort, pageNumber, params.loadSize)
            val endOfPaginationReached = response.body()?.meta?.isEnd!!

            val data = response.body()?.documents!!
            val prevKey = if (pageNumber == STARTING_PAGE_INDEX) null else pageNumber - 1
            val nextKey = if (endOfPaginationReached) {
                null
            } else {
                // initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                pageNumber + (params.loadSize / PAGING_SIZE)
            }
            LoadResult.Page(
                data = data,
                prevKey = prevKey,
                nextKey = nextKey,
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, FBook>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }
}