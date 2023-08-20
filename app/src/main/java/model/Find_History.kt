package model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class Find_History (
    @PrimaryKey val uid: Int?,
    @ColumnInfo(name="keyword") val keyword: String?
)