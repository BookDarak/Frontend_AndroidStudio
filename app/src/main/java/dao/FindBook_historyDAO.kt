package dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import model.FindBook_historymodel

@Dao
interface FindBook_historyDAO {
    // 전부 가져옴
    @Query("SELECT * FROM FindBook_historymodel")
    fun getAll(): List<FindBook_historymodel>

    // 검색 작업이 일어날 때 insert
    @Insert
    fun insertHistory(history: FindBook_historymodel)

    // x 눌렀을 때 키워드 지워줌
    @Query("DELETE FROM FindBook_historymodel WHERE keyword = :keyword")
    fun delete(keyword: String)
}