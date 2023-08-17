package com.cookandroid.bookdarak_1.data.DB



import androidx.paging.PagingSource
import androidx.room.*
import com.cookandroid.bookdarak_1.data.model.FBook
import kotlinx.coroutines.flow.Flow

@Dao
interface BookSearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(fbook: FBook)

    @Delete
    suspend fun deleteBook(fbook: FBook)


    //FLOW 응답
    @Query("SELECT * From fbooks")
    fun getFavoriteBooks(): Flow<List<FBook>>

    //페이징 처리
    @Query("SELECT * FROM fbooks")
    fun getFavoritePagingBooks(): PagingSource<Int, FBook>
}