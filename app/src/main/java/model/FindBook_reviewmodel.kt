package model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FindBook_reviewmodel (
    @PrimaryKey val title: String?,
    @ColumnInfo(name="review") val review: String?
)