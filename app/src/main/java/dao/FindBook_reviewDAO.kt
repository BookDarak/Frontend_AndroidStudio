package dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import model.FindBook_reviewmodel

@Dao
interface FindBook_reviewDAO {

    @Query("SELECT * FROM FindBook_reviewmodel WHERE string = :string")
    fun getOneReview(title: String): FindBook_reviewmodel

    // 같은 값이오면 새로운 거로 대체.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReview(review: FindBook_reviewmodel)
}