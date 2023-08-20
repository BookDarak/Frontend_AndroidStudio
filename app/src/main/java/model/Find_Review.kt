package model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Find_Review (

    @PrimaryKey
    val isbn : String,
    @ColumnInfo(name="Find_Review") val Find_Review: String?
)