package dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cookandroid.bookdarak_1.model.Review
import retrofit2.http.Query

@Dao
interface FindBook_reviewDAO {

    @Query("SELECT * FROM review WHERE id = :id")
    fun getOneReview(id: Int): Review

    // 같은 값이오면 새로운 거로 대체.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReview(review: Review)
}