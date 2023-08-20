package com.cookandroid.bookdarak_1.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import model.Find_Review


@Dao
interface ReviewDao {

    @Query("SELECT * FROM Find_Review WHERE isbn = :isbn")
    fun getOneReview(isbn: String): Find_Review

    // 같은 값이오면 새로운 거로 대체.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReview(findReview: Find_Review)
}