package com.cookandroid.bookdarak_1.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import model.Find_History

@Dao
interface HistoryDao {
    // 전부 가져오는 는
    @Query("SELECT * FROM history")
    fun getAll(): List<Find_History>

    // 검색 작업이 일어날 때 insert
    @Insert
    fun insertHistory(history: Find_History)

    // x 눌렀을 때 키워드 지워주
    @Query("DELETE FROM history WHERE keyword = :keyword")
    fun delete(keyword: String)
}