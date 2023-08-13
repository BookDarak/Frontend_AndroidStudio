package dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cookandroid.bookdarak_1.model.History
import retrofit2.http.Query

@Dao
interface FindBook_historyDAO {
    // 전부 가져오는 는
    @Query("SELECT * FROM history")
    fun getAll(): List<History>

    // 검색 작업이 일어날 때 insert
    @Insert
    fun insertHistory(history: History)

    // x 눌렀을 때 키워드 지워주
    @Query("DELETE FROM history WHERE keyword = :keyword")
    fun delete(keyword: String)
}