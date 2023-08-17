package com.cookandroid.bookdarak_1.data.DB

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cookandroid.bookdarak_1.data.model.FBook


@Database(
    entities = [FBook::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(OrmConverter::class)
abstract class BookSearchDatabase : RoomDatabase(){

    abstract fun bookSearchDao(): BookSearchDao




}